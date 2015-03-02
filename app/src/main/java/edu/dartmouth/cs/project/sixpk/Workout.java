package edu.dartmouth.cs.project.sixpk;


import java.util.ArrayList;
import java.util.Random;


/*
workout flow:
    all possible exercises are stored in db
    main onCreate() { fetch all exercises }
    pass arraylist of exercises, input time, input difficulty into Workout constructor
    create Workout object with exercises from different muscle groups
    after exercise, change feedback (difficulty) of each exercise
    or shared preferences for feedback instead of altering what’s in the db?
    add workout stats to stats page
 */

public class Workout {
    private final int DEFAULT_DURATION = 120; // seconds

    /*
    need to decide if we should store arraylist of Exercise objects
    or arraylist of Exercise IDs and then get them from the database separately in the activity
    I actually am into the second one right now so I think I'll implement that right now
     */

    public Workout(ArrayList<Exercise> exercises, int time, int diff) {
        dateTime = System.currentTimeMillis(); // I guess?

        formWorkout(exercises, time, diff);
    }

    // parallel arraylists to hold exercise IDs, respective durations
    private ArrayList<Integer> exerciseIds;
    private ArrayList<Integer> durations; // in seconds
    private long dateTime; // in milliseconds but we can convert it

    /*
    forming workout:
        higher input difficulty (1 to 3) increases exercise duration
        higher exercise difficulty (-10 to 10) decreases exercise duration
        at least one exercise from each muscle group (1 to 3)
     */
    public void formWorkout(ArrayList<Exercise> exercises, int time, int diff) {


    }

    // saving for later but I'll use it for formWorkout
    // gonna need the number of exercises per ab group
    // and possibly get unique random number too
    private int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    // format millis to string format
    public String getDateFormat() {
        // TODO
        return "date stuff";
    }

}
