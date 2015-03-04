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

    // Format duration for display
    public static String formatDuration(int sec) {
        String time;

        int min = sec / 60;         // Get minutes from seconds
        int seconds = sec % 60;     // Get remaining seconds after minutes

        // If duration was less than 1 minute, show time in seconds
        if (min == 0)
            time = seconds + "secs";
            // Else show time in minutes and seconds
        else
            time = min + "mins " + seconds + "secs";

        return time;
    }

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
