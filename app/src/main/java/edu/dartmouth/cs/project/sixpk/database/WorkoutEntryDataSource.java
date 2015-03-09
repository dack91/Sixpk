package edu.dartmouth.cs.project.sixpk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutEntryDataSource {

    public final String TAG = "WorkoutEntryDataSource";

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allWorkoutColumns = {MySQLiteHelper.COLUMN_WORKOUT_ID,
            MySQLiteHelper.COLUMN_WORKOUT_DURATION, MySQLiteHelper.COLUMN_WORKOUT_DURATION_LIST, MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME,
            MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST};
    private String[] allAblogColumns = {MySQLiteHelper.COLUMN_ABLOG_ID,
            MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP,
            MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY, MySQLiteHelper.COLUMN_ABLOG_NAME,
            MySQLiteHelper.COLUMN_ABLOG_FILEPATH};

    public WorkoutEntryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    // call in onResume
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // call in onPause
    public void close() {
        dbHelper.close();
    }

    // Insert a item given each column value
    public long insertWorkoutEntry(Workout entry) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME, String.valueOf(entry.getDateTime()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DURATION, entry.getDuration());
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DURATION_LIST, Arrays.toString(entry.getDurationList()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, Arrays.toString(entry.getFeedBackList()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST, Arrays.toString(entry.getExerciseIdList()));

        long insertId = database.insert(MySQLiteHelper.TABLE_WORKOUTS, null, values);

        // query to make sure it inserted correctly
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
                allWorkoutColumns, MySQLiteHelper.COLUMN_WORKOUT_ID + "=" + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        long toRet = cursor.getLong(0);
        cursor.close();

        // if insert fails, return -1
        if (toRet != insertId) return -1;
        return insertId;
    }

    // change the workout entry for feedback
    public void updateWorkoutEntry(long rowId, Workout entry) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME, String.valueOf(entry.getDateTime()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DURATION, entry.getDuration());
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DURATION_LIST, Arrays.toString(entry.getDurationList()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, Arrays.toString(entry.getFeedBackList()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST, Arrays.toString(entry.getExerciseIdList()));
        database.update(MySQLiteHelper.TABLE_WORKOUTS, values, MySQLiteHelper.COLUMN_WORKOUT_ID + "=" + rowId, null);
    }

    // insert an ablog object into the database
    public long insertAblogEntry(AbLog ablog) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, ablog.getAblogNumber());
        values.put(MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP, ablog.getMuscleGroup());
        values.put(MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY, Arrays.toString(ablog.getDifficultyArray()));
        values.put(MySQLiteHelper.COLUMN_ABLOG_NAME, ablog.getName());
        values.put(MySQLiteHelper.COLUMN_ABLOG_FILEPATH, ablog.getFilePath());

        long insertId = database.insert(MySQLiteHelper.TABLE_ABLOG, null, values);
        return insertId; // will be -1 if it failed
    }

    // update an ablog row
    public void updateAbLog(long rowId, AbLog ablog) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, ablog.getAblogNumber());
        values.put(MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP, ablog.getMuscleGroup());
        values.put(MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY, Arrays.toString(ablog.getDifficultyArray()));
        values.put(MySQLiteHelper.COLUMN_ABLOG_NAME, ablog.getName());
        values.put(MySQLiteHelper.COLUMN_ABLOG_FILEPATH, ablog.getFilePath());
        database.update(MySQLiteHelper.TABLE_ABLOG, values, MySQLiteHelper.COLUMN_ABLOG_ID + "=" + rowId, null);
    }

    // remove a workout from the database given a rowId
    public void removeWorkoutEntry(long rowIndex) {
        database.delete(MySQLiteHelper.TABLE_WORKOUTS, MySQLiteHelper.COLUMN_WORKOUT_ID
                + " = " + rowIndex, null);
    }

    // remove an ablog from the database given a rowId
    public void removeAblogEntry(long rowIndex) {
        database.delete(MySQLiteHelper.TABLE_ABLOG, MySQLiteHelper.COLUMN_ABLOG_ID
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public Workout fetchWorkoutByIndex(long rowId) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
                allWorkoutColumns, MySQLiteHelper.COLUMN_WORKOUT_ID + "=" + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        return cursorToWorkout(cursor);
    }

    // Query a specific entry by its index.
    public AbLog fetchAbLogByIndex(long rowId) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ABLOG,
                allAblogColumns, MySQLiteHelper.COLUMN_ABLOG_ID + "=" + rowId,
                null, null, null, null);
        cursor.moveToFirst();
        return cursorToAblog(cursor);
    }

    // Query a specific entry by its index.
    public AbLog fetchAbLogByIdentifier(long rowId) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ABLOG,
                allAblogColumns, MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID + "=" + rowId,
                null, null, null, null);
        cursor.moveToFirst();
        return cursorToAblog(cursor);
    }

    // query the workout table, return in an arraylist
    public ArrayList<Workout> fetchWorkoutEntries() {
        ArrayList<Workout> entries = new ArrayList<Workout>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
                allWorkoutColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Workout mEntry = cursorToWorkout(cursor);
            entries.add(mEntry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    // query the ablog table, return in an arraylist
    public ArrayList<AbLog> fetchAbLogEntries() {
        ArrayList<AbLog> entries = new ArrayList<AbLog>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ABLOG,
                allAblogColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AbLog mEntry = cursorToAblog(cursor);
            entries.add(mEntry);
            cursor.moveToNext();
        }

        cursor.close();
        return entries;
    }

    // Get ab exercise name from ID
    public String getNameById(int id) {
        ArrayList<AbLog> allExercises = fetchAbLogEntries();

        for (AbLog curr : allExercises) {
            if (id == curr.getAblogNumber()) {
                return curr.getName();
            }
        }
        return null;
    }

    // return a gif filepath from an abLogNumber
    public String getFilePathById(int id) {
        ArrayList<AbLog> allExercises = fetchAbLogEntries();

        for (AbLog curr : allExercises) {
            if (id == curr.getAblogNumber()) {
                return curr.getFilePath();
            }
        }
        return null;
    }

    // get a workout from a cursor, convert strings in db to arrays
    private Workout cursorToWorkout(Cursor cursor) {
        Workout entry = new Workout();

        entry.setDuration(cursor.getInt(1));
        entry.setDateTime(cursor.getLong(3));

        String durationString = cursor.getString(2);
        if (!durationString.contains("null")) {
            String[] s = durationString.substring(1, durationString.length() - 1).split(", ");
            int[] numbers = new int[s.length];
            for (int curr = 0; curr < s.length; curr++)
                numbers[curr] = Integer.parseInt(s[curr]);
            entry.setDurationList(numbers);
        }

        String difficultyString = cursor.getString(4);
        if (!difficultyString.contains("null")) {
            String[] s = difficultyString.substring(1, difficultyString.length() - 1).split(", ");
            int[] numbers = new int[s.length];
            for (int curr = 0; curr < s.length; curr++)
                numbers[curr] = Integer.parseInt(s[curr]);
            entry.setFeedBackList(numbers);
        }

        String feedbackString = cursor.getString(5);
        if (!feedbackString.contains("null")) {
            String[] s1 = feedbackString.substring(1, feedbackString.length() - 1).split(", ");
            int[] numbers1 = new int[s1.length];
            for (int curr = 0; curr < s1.length; curr++)
                numbers1[curr] = Integer.parseInt(s1[curr]);
            entry.setExerciseIdList(numbers1);
        }
        return entry;
    }

    // get an ablog from a cursor
    private AbLog cursorToAblog(Cursor cursor) {
        AbLog abLog = new AbLog();
        abLog.setId(cursor.getInt(0));
        abLog.setAblogNumber(cursor.getInt(1));
        abLog.setMuscleGroup(cursor.getInt(2));

        // parse the string for the difficulty array
        String difficultyString = cursor.getString(3);
        String[] s = difficultyString.substring(1, difficultyString.length() - 1).split(", ");
        int[] numbers = new int[s.length];
        for (int curr = 0; curr < s.length; curr++)
            numbers[curr] = Integer.parseInt(s[curr]);
        abLog.setDifficultyArray(numbers);

        abLog.setName(cursor.getString(4));
        abLog.setFilePath(cursor.getString(5));
        return abLog;
    }
}
