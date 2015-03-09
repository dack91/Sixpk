package edu.dartmouth.cs.project.sixpk;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by 6pk on 2/16/15.
 */
public class Globals {

    public static final String WORKOUT_DURATION_KEY = "edu.dartmouth.cs.project.sixpk.WORKOUT_DURATION_KEY";
    public static final String WORKOUT_DIFFICULTY_KEY = "edu.dartmouth.cs.project.sixpk.WORKOUT_DIFFICULTY_KEY";
    public static final String WORKOUT_ID_KEY = "edu.dartmouth.cs.project.sixpk.WORKOUT_ID_KEY";

    public static final int WORKOUT_EASY = 0;
    public static final int WORKOUT_MED = 1;
    public static final int WORKOUT_HARD = 2;

    public static final int MAX_TIME = 15;
    public static final int MIN_TIME = 5;
    public static final int DEFAULT_TIME = (MAX_TIME + MIN_TIME) / 2;  // If no time is passed through intent, build 10min workout

    public static ArrayList<String> ALL_EXERCISES = new ArrayList<>();

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

    public static String formatTime(int sec) {
        String time;

        int min = sec / 60;         // Get minutes from seconds
        int seconds = sec % 60;     // Get remaining seconds after minutes

        time = String.format("%02d", min) + ":" + String.format("%02d", seconds);

        return time;
    }

    // before calling this, fetchAllWorkouts and then add each dateTime into the long[] for this function
    public static long findMostCommonDate(long[] times) {
        // For each dateTime in the long[], group it into a time range
        // (ei. 12am-3am, 3-6am, 6-9am, 9-12pm, 12-3pm, 3-6pm, 6-9pm, 9-12am)
        // 00:00, 03:00, 06:00, 09:00, 12:00, 15:00, 18:00, 21:00
        // Then calculate which of the time ranges is the most frequently used for workouts
        // send the notification at that beginning of that time range

        // Parallel arrays hold group start time and frequency of workouts in range
        int range = 3;  // ranges are 3 hours long
        int[] timeGroups = new int[]{0, 3, 6, 9, 12, 15, 18, 21};
        int[] timeGroupFrequencies = new int[8];

        // Group all workouts into appropriate range
        for (long time : times) {
            String date = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(time));
            String[] timeString = date.split(":");
            int hour = Integer.parseInt(timeString[0]);
            int min = Integer.parseInt(timeString[1]);

            // round minutes to the closest hour
            if (min >= 30) {
                hour++;
            }

            // Find group and increment frequency
            int j = 0;
            while (j < timeGroups.length - 1 && hour > timeGroups[j] + range) {
                j++;
            }
            timeGroupFrequencies[j] = timeGroupFrequencies[j] + 1;
        }

        int maxValue = 0;   // value is the index into the timeGroups int[]
        int maxCount = 0;   // frequency per time group

        // Find highest frequency time range
        for (int i = 0; i < timeGroupFrequencies.length; i++) {
            if (timeGroupFrequencies[i] > maxCount) {
                maxCount = timeGroupFrequencies[i];
                maxValue = i;
            }
        }

        // Set time of most frequent workouts
        Calendar c = Calendar.getInstance();
        c.set(2015, 3, 6, timeGroups[maxValue], 0);

        return c.getTimeInMillis();
    }
}
