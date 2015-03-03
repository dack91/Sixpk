package edu.dartmouth.cs.project.sixpk;


import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import edu.dartmouth.cs.project.sixpk.database.AbLog;


/*
workout flow:
    all possible exercises are stored in db
    main onCreate() { fetch all exercises }
    pass ArrayList of exercises, input time, input difficulty into Workout constructor
    create Workout object with exercises from different muscle groups
    after exercise, change feedback (difficulty) of each exercise
    or shared preferences for feedback instead of altering what’s in the db?
    add workout stats to stats page
 */

public class Workout {
    private final int MUSCLE_GROUPS = 3; // how many muscle groups are there

    private final int[] DEFAULT_DURATIONS = new int[MUSCLE_GROUPS];

    public Workout(ArrayList<AbLog> exercises, int time, int diff) {
        dateTime = System.currentTimeMillis(); // I guess?
        duration = time * 60;
        difficulty = diff;

        DEFAULT_DURATIONS[0] = 90;
        DEFAULT_DURATIONS[1] = 120;
        DEFAULT_DURATIONS[2] = 150;

        exerciseIds = new ArrayList<Integer>();
        durations = new ArrayList<Integer>();

        formWorkout(exercises, time, diff);
        exerciseIdList = convertToIntArray(exerciseIds);
    }

    // parallel arraylists to hold exercise IDs, respective durations
    ArrayList<Integer> exerciseIds; // not stored in db
    ArrayList<Integer> durations; // in seconds, not stored in db

    private long dateTime; // in milliseconds but we can convert it
    private int difficulty; //Easy, medium, hard
    private int[] exerciseIdList; //i.e. [1,5,2] where 1 = crunches, 2 = planks, etc.
    private double[] feedBackList; // corresponds to ordering of exercise id list
    private int duration; //length of workout
    private long id; // database row

    /*
    forming workout:
        time in minutes
        higher input difficulty (1 to 3) increases exercise duration
            and selects more difficult exercises
        higher exercise difficulty (-10 to 10) decreases exercise duration
        at least one exercise from each muscle group (1 to 3)

        sort exercises by muscle group
        sort muscle groups by difficulty
        set random number bounds based on input difficulty
        unique random numbers based on total time of workout


        difficulty array 1-10
        array[0] is most recent
     */
    public void formWorkout(ArrayList<AbLog> exercises, int time, int diff) {
        int def_duration = DEFAULT_DURATIONS[diff - 1];

        int total = (time * 60) / def_duration; // approx how many workouts
        int subset = total / MUSCLE_GROUPS; // how many workouts per muscle group

        // create the muscle list
        for (int m = 0; m < MUSCLE_GROUPS; m++) {
            // sort the exercises in one muscle group by feedback difficulty
            AbLog[] sorted = sortByDifficulty( chosenGroup(exercises, m) );
            int min = 0, max = sorted.length;

            if (diff == 1) { // only pick easier exercises if "easy" difficulty is selected
                max -= sorted.length / 3;
            } else if (diff == 3) {
                min += sorted.length / 3;
            }

            int[] rands = uniqueRands(subset, min, max);

            // add randomly selected exercises to the list
            for (Integer r : rands) {
                exerciseIds.add(sorted[r].getAblogId());

                // shorten or extend the duration based on feedback
                int alter = -(sorted[r].getDifficultyArray()[0] * 5);
                durations.add(def_duration + alter);
            }
        }

        int extra = time * 60;
        for (Integer dur : durations) {
            extra -= dur;
        }

        correctTiming(extra);
        shuffle();
    }

    // returns a random integer between min and max inclusive
    private int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    // returns a list of unique random ints within a range
    private int[] uniqueRands(int num, int min, int max) {
        int[] rands = new int[num];

        int i = 0;
        while (i < num) {
            int temp = randInt(min, max);
            boolean chosen = false;

            // check if the number has been chosen before
            if (i > 0) {
                for (int j = 0; j < i; j++) {
                    if (rands[j] == temp) {
                        chosen = true;
                    }
                }
            }
            if (chosen) continue;

            rands[i] = temp;
            i++;
        }
        return rands;
    }

    // return all exercises of one muscle group
    private ArrayList<AbLog> chosenGroup(ArrayList<AbLog> exercises, int muscleGroup) {
        ArrayList<AbLog> subset = new ArrayList<AbLog>();

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
    private void correctTiming(int leftover) {

        while (true) {
            if (leftover < -30) {
                // get rid of last exercise if time is way over
                exerciseIds.remove(exerciseIds.size() - 1);
                leftover += durations.get(durations.size() - 1);
                durations.remove(durations.size() - 1);

            } else if (leftover >= -30 && leftover <= 30) {
                // no editing if leftover is within a half minute
                break;

            } else {
                // disperse the leftover seconds over all exercises if it's too large
                int disp = 5 * (Math.round( (leftover / durations.size()) / 5)); // round to nearest 5

                for (int i = 0; i < durations.size(); i++) {
                    durations.set(i, durations.get(i) + disp);
                }
                break;
            }
        }
    }

    private AbLog[] swap(AbLog[] list, int a, int b) {
        AbLog temp = list[b];
        list[b] = list[a];
        list[a] = temp;

        return list;
    }

    private void swap(int a, int b) {
        int id = exerciseIds.get(b);
        int dur = durations.get(b);

        exerciseIds.set(b, exerciseIds.get(a));
        durations.set(b, durations.get(a));

        exerciseIds.set(a, id);
        durations.set(a, dur);
    }

    // mix up the order of the exercises (ArrayList)
    private void shuffle() {
        int rand = randInt(1, 20); // arbitrary number of swaps
        int max = exerciseIds.size();

        for (int i = 0; i < rand; i++) {
            swap(randInt(0, max), randInt(0, max));
        }
    }

    private int[] convertToIntArray(ArrayList<Integer> ex) {
        int[] ids = new int[ex.size()];
        for (int i=0; i < ids.length; i++) {
            ids[i] = ex.get(i).intValue();
        }
        return ids;
    }

    public double[] getFeedBackList() {
        return feedBackList;
    }

    public void setFeedBackList(double[] feedBackList) {
        this.feedBackList = feedBackList;
    }

    public int[] getExerciseIdList() {
        return exerciseIdList;
    }

    public void setExerciseIdList(int[] exerciseIdList) {
        this.exerciseIdList = exerciseIdList;
    }
}



