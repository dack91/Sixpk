package edu.dartmouth.cs.project.sixpk;

import java.util.ArrayList;

/**
 * Created by 6pk on 2/16/15.
 */
public class Globals {

    public static final String WORKOUT_DURATION_KEY = "edu.dartmouth.cs.codeit.sixpk.WORKOUT_DURATION_KEY";
    public static final String WORKOUT_DIFFICULTY_KEY = "edu.dartmouth.cs.codeit.sixpk.WORKOUT_DIFFICULTY_KEY";

    public static final int WORKOUT_EASY = 0;
    public static final int WORKOUT_MED = 1;
    public static final int WORKOUT_HARD = 2;

    public static final int DEFAULT_TIME = 10;  // If no time is passed through intent, build 10min workout


    // Test list of workouts for listview -- TEMPORARY
    public static ArrayList<String> test = new ArrayList<String>(){{
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
    }};
}
