package edu.dartmouth.cs.codeit.sixpk;


import java.util.ArrayList;
import java.util.Random;

// also why the f is this our package name
// import edu.dartmouth.cs.codeit.sixpk.Exercise;


public class Workout {
    /*
    need to decide if we should store arraylist of Exercise objects
    or arraylist of Exercise IDs and then get them from the database separately in the activity
    I actually am into the second one right now so I think I'll implement that right now
     */

    public Workout(int time, int diff) {
        dateTime = System.currentTimeMillis(); // I guess?

        formWorkout(time, diff);
    }

    // parallel arraylists to hold exercise IDs, respective durations
    private ArrayList<Integer> exerciseIds;
    private ArrayList<Integer> durations; // in seconds
    private long dateTime; // in milliseconds but we can convert it

    // takes in totalTime and each exercise group
    // forms the durations from different exercises based on difficulty
    // this is the closest thing we have to an algorithm in this app
    public void formWorkout(int time, int diff) {

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
