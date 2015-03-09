package edu.dartmouth.cs.project.sixpk.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Random;

import edu.dartmouth.cs.project.sixpk.Globals;

public class Workout {

    private long dateTime; // in milliseconds but we can convert it
    private int difficulty; // easy, medium, hard correspond to 0-2
    private int[] exerciseIdList; // array of ablogNumbers
    private int[] feedBackList; // corresponds to ordering of exercise id list
    private int duration; // length of workout in seconds
    private long id; // database row

    // parallel arraylists to hold exercise IDs, respective durations
    ArrayList<Integer> exerciseIds = new ArrayList<>(); // not stored in db
    ArrayList<Integer> durations = new ArrayList<>(); // in seconds, not stored in db

    int[] durationList;

    private final int MUSCLE_GROUPS = 3; // how many muscle groups there are

    private final int[] DEFAULT_DURATIONS = new int[MUSCLE_GROUPS];

    // constructor for datasource
    public Workout() {}

    // constructor from preview activity
    public Workout(ArrayList<AbLog> exercises, int time, int diff) {
        // get time that the workout was started
        dateTime = Calendar.getInstance().getTimeInMillis();
        duration = time * 60;
        difficulty = diff;

        // set the default durations for each difficulty in seconds
        DEFAULT_DURATIONS[0] = 90;
        DEFAULT_DURATIONS[1] = 105;
        DEFAULT_DURATIONS[2] = 120;

        // create the order of exercises for the workout
        formWorkout(exercises, time, diff);

        exerciseIdList = convertToIntArray(exerciseIds);
        durationList = convertToIntArray(durations);
    }


    /*
    parameters: arraylist of all exercises, input time in mins, input difficulty (0-2)

    sort exercises by muscle group
    sort muscle groups by difficulty
    set random number bounds based on input difficulty
    unique random numbers based on total time of workout
    shorten lengths of workouts if difficulty is higher
    reorder of workouts to not have muscle groups all together
     */
    public void formWorkout(ArrayList<AbLog> exercises, int time, int diff) {
        int def_duration = DEFAULT_DURATIONS[diff];

        double divide = (time * 60) / (double) def_duration;
        double total = Math.ceil(divide); // approx how many workouts
        double subset = Math.ceil(total / MUSCLE_GROUPS); // how many workouts per muscle group

        // create the muscle list
        for (int m = 1; m <= MUSCLE_GROUPS; m++) { // muscle groups are labeled 1-3
            // sort the exercises in one muscle group by feedback difficulty
            AbLog[] sorted = sortByDifficulty(chosenGroup(exercises, m));
            int min = 0, max = sorted.length; // max is exclusive

            if (diff == Globals.WORKOUT_EASY) { // only pick easier exercises if "easy" difficulty is selected
                max -= (int) Math.ceil(sorted.length / 3); // arbitrary pick the bottom 2/3 of exercises
            } else if (diff == Globals.WORKOUT_HARD) {
                min += (int) Math.ceil(sorted.length / 3);
            }

            // randomly choose some exercises within a range
            int[] rands = uniqueRands( (int) subset, min, max);

            // add randomly selected exercises to the list
            for (int rand : rands) {
                if(sorted.length > rand){
                    AbLog sortedIndex = sorted[rand];
                    exerciseIds.add(sortedIndex.getAblogNumber());

                    // difficulty is 0-10, 5 is default
                    int[] diffs = sorted[rand].getDifficultyArray(); // always length 3
                    int feedback = 0;

                    if (diffs.length < 3) {
                        feedback = diffs[0] - 5;
                    } else {
                        // double weight most recent feedback, take average, and correct to 0
                        feedback += (diffs[0] * 2) + diffs[1] + diffs[2];
                        feedback /= 4;
                        feedback -= 5;
                    }

                    // shorten or extend the duration based on feedback
                    // multiply by 5 seconds per unit, negate because easier is lower numbers which are longer workouts
                    int alter = -(feedback * 5);
                    // alter = randInt(-5, 6) * 5; // for debugging, use random difficulties
                    durations.add(def_duration + alter);
                }
            }
        }

        // check how many seconds off from the input time
        int extra = time * 60;
        for (Integer dur : durations) {
            extra -= dur;
        }

        // correct for extra/not enough seconds in workout
        correctTiming(extra, def_duration);

        // change order of exercises so two exercises with the same muscle groups aren't in a row
        reorder(exercises);
    }

    // returns a random integer between min inclusive and max exclusive
    private int randInt(int min, int max) {
        Random rand = new Random();
        // return rand.nextInt((max - min) + 1) + min; // for min to max inclusive
        return rand.nextInt(max - min) + min;
    }

    // return an int array of unique random ints within a min inclusive to max exclusive range
    private int[] uniqueRands(int num, int min, int max) {
        // fill an arraylist with numbers in order
        ArrayList<Integer> order = new ArrayList<>(max - min);
        for (int i = min; i < max; i++) {
            order.add(i);
        }

        // pop out numbers randomly from the ordered list
        int[] rands = new int[num];
        for (int i = 0; i < num; i++){
            if (order.size() == 0) return Arrays.copyOfRange(rands, 0, i);
            rands[i] = order.remove((int) (Math.random() * order.size()));
        }

        return rands;
    }

    // return all exercises of one muscle group
    private ArrayList<AbLog> chosenGroup(ArrayList<AbLog> exercises, int muscleGroup) {
        ArrayList<AbLog> subset = new ArrayList<>();

        for (AbLog entry : exercises) {
            if (entry.getMuscleGroup() == muscleGroup) {
                subset.add(entry);
            }
        }

        return subset;
    }

    // returns exercise array from lowest to highest difficulty
    private AbLog[] sortByDifficulty(ArrayList<AbLog> subset) {
        int length = subset.size();
        AbLog[] sorted = new AbLog[length];

        for (int i = 0; i < length; i++) {
            sorted[i] = subset.get(i);
        }

        // using bubble sort for convenience
        for (int i = 0; i < length - 1; i++) {
            for (int j = 1; j < length; j++) {

                if (sorted[j - 1].getDifficultyArray()[0] > sorted[j].getDifficultyArray()[0]) {
                    sorted = swap(sorted, j - 1, j);
                }
            }
        }

        return sorted;
    }

    // takes the leftover seconds and either deletes workouts or spreads them out
    // to correct the total time
    private void correctTiming(int leftover, int def_duration) {
        while (true) {
            if (exerciseIds.size() <= 1) {
                break;
            }

            if (leftover < -def_duration) {
                // get rid of last exercise if time is way over
                int rand = randInt(0, exerciseIds.size() - 1);
                exerciseIds.remove(rand);
                leftover += durations.get(rand);
                durations.remove(rand);

            } else if (leftover >= -def_duration && leftover < -30) {
                // take away the leftover seconds over all exercises if it's too small
                int disp = 0;
                if (durations.size() != 0){
                    disp = 5 * (Math.round(( (-leftover) / durations.size()) / 5)); // round to nearest 5
//                    leftover += disp * durations.size();
                    // causing an infinite loop here because disp rounds down to 0 but leftover is still -35
                    // could correct the else if bounds, but I'll just add breaks instead
                }

                for (int i = 0; i < durations.size(); i++) {
                    durations.set(i, durations.get(i) - disp);
                }
                break;

            } else if (leftover >= -30 && leftover < 30) {
                // no editing if leftover is within a half minute
                break;

            } else {
                // disperse the leftover seconds over all exercises if it's too large
                int disp = 0;
                if (durations.size() != 0){
                    disp = 5 * (Math.round((leftover / durations.size()) / 5)); // round to nearest 5
                    //leftover -= disp * durations.size();
                }

                for (int i = 0; i < durations.size(); i++) {
                    durations.set(i, durations.get(i) + disp);
                }
                break;
            }
        }
    }

    // swap two values in an ablog array
    private AbLog[] swap(AbLog[] list, int a, int b) {
        AbLog temp = list[b];
        list[b] = list[a];
        list[a] = temp;

        return list;
    }

    // swap the exercise Ids and durations
    private void swap(int a, int b) {
        int id = exerciseIds.get(b);
        int dur = durations.get(b);

        exerciseIds.set(b, exerciseIds.get(a));
        durations.set(b, durations.get(a));

        exerciseIds.set(a, id);
        durations.set(a, dur);
    }

    // order exercises to have none of the same muscle groups in a row
    private void reorder(ArrayList<AbLog> exercises) {
        // randomly pick order of muscle groups
        int[] order = uniqueRands(MUSCLE_GROUPS, 1, MUSCLE_GROUPS + 1);
        int o = 0;

        for (int i = 0; i < exerciseIds.size(); i++) {
            int cur = InitialAbInputs.getGroupFromNum(exerciseIds.get(i));

            if (cur != order[o]) {

                // check for another exercise in the list with the desired muscle group
                for (int j = i + 1; j < exerciseIds.size(); j++) {
                    if (InitialAbInputs.getGroupFromNum(exerciseIds.get(j)) == order[o]) {
                        swap(i, j);
                        break;
                    }
                }
            }

            o++;
            if (o >= MUSCLE_GROUPS) o = 0;
        }
    }

    // arraylist to int[]
    private int[] convertToIntArray(ArrayList<Integer> al) {
        int[] new_list = new int[al.size()];

        for (int i = 0; i < new_list.length; i++) {
            new_list[i] = al.get(i).intValue();
        }

        return new_list;
    }

    // Remove all exercises not completed in workoutActivity
    public void removeRemainingExercises(int index) {
        // Repopulate arrayList of exercises and durations to remove index
        exerciseIds = new ArrayList<Integer>();
        durations = new ArrayList<Integer>();

        int i = 0;
        while (i < index) {
            exerciseIds.add(exerciseIdList[i]);
            durations.add(durationList[i]);
            i++;
        }

        // Update int[]
        exerciseIdList = convertToIntArray(exerciseIds);
        durationList = convertToIntArray(durations);

        // Update workout duration
        setTotalTime();
    }

    // Remove exercise from Preview
    public void removeSelectedExercise(int index) {
        // Remove from workout
        exerciseIds.remove(index);
        durations.remove(index);

        // Update int[]
        exerciseIdList = convertToIntArray(exerciseIds);
        durationList = convertToIntArray(durations);

        // Update workout duration
        setTotalTime();
    }

    public int getActualTime() {
        int time = 0;
        for (int i : durationList) {
            time += i;
        }
        return time;
    }

    public void setTotalTime() {
        this.duration = getActualTime();
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int[] getFeedBackList() {
        return feedBackList;
    }

    public void setFeedBackList(int[] feedBackList) {
        this.feedBackList = feedBackList;
    }

    public int[] getExerciseIdList() {
        return exerciseIdList;
    }

    public void setExerciseIdList(int[] exerciseIdList) {
        this.exerciseIdList = exerciseIdList;
    }

    public int[] getDurationList() {
        return durationList;
    }

    public void setDurationList(int[] durationList) {
        this.durationList = durationList;
    }

    public int getDuration() {
        return duration;
    }

    public long getDateTime() {
        return dateTime;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
