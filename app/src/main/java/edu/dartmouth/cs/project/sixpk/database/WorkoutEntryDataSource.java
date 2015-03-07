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
            MySQLiteHelper.COLUMN_WORKOUT_DURATION, MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME,
            MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST};
    private String[] allAblogColumns = {MySQLiteHelper.COLUMN_ABLOG_ID,
            MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP,
            MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY, MySQLiteHelper.COLUMN_ABLOG_NAME,
            MySQLiteHelper.COLUMN_ABLOG_FILEPATH};

    public WorkoutEntryDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a item given each column value
    public long insertWorkoutEntry(Workout entry) {

        Log.d(TAG, "Inserting entry:\nid:" + entry.getId() + "\ndate-time: " + entry.getDateTime());
        Log.d(TAG, "duration: " + entry.getDuration() + "\nfeedbackList: " + Arrays.toString(entry.getFeedBackList()));
        Log.d(TAG, "exercise id list: " + Arrays.toString(entry.getExerciseIdList()) + "\ninsert end");
        ContentValues values = new ContentValues();
        //     values.put(MySQLiteHelper.COLUMN_WORKOUT_ID, entry.getId());
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME, String.valueOf(entry.getDateTime()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_DURATION, entry.getDuration());
        values.put(MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, Arrays.toString(entry.getFeedBackList()));
        values.put(MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST, Arrays.toString(entry.getExerciseIdList()));

        long insertId = database.insert(MySQLiteHelper.TABLE_WORKOUTS, null, values);
        Log.d(TAG, "Insertion id: " + insertId);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
                allWorkoutColumns, MySQLiteHelper.COLUMN_WORKOUT_ID + "=" + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        long toRet = cursor.getLong(0);
        cursor.close();
        if (toRet != insertId) {
            Log.d(TAG, "Query not matching");
            return -1;
        }
        return insertId;
    }

    // Insert a item given each column value
    public long insertAblogEntry(AbLog ablog) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, ablog.getAblogNumber());
        values.put(MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP, ablog.getMuscleGroup());
        values.put(MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY, Arrays.toString(ablog.getDifficultyArray()));
        values.put(MySQLiteHelper.COLUMN_ABLOG_NAME, ablog.getName());
        values.put(MySQLiteHelper.COLUMN_ABLOG_FILEPATH, ablog.getFilePath());

        // Log.d("Inserting ablog: ", ablog.getAblogNumber() + "");

        long insertId = database.insert(MySQLiteHelper.TABLE_ABLOG, null, values);

        return insertId;
    }


    // Remove an entry by giving its index
    public void removeWorkoutEntry(long rowIndex) {
        database.delete(MySQLiteHelper.TABLE_WORKOUTS, MySQLiteHelper.COLUMN_WORKOUT_ID
                + " = " + rowIndex, null);
    }

    // Remove an entry by giving its index
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
        String mId = String.valueOf(rowId);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ABLOG,
                allAblogColumns, "?=?",
                new String[]{MySQLiteHelper.COLUMN_ABLOG_ID, mId},
                null, null, null);
        cursor.moveToFirst();
        return cursorToAblog(cursor);
    }

    // Query the entire table, return all rows
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
        // make sure to close the cursor
        cursor.close();
        return entries;
    }

    // Query the entire table, return all rows
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

    private Workout cursorToWorkout(Cursor cursor) {
        Workout entry = new Workout();
//        if(cursor.getCount()==0){
//            return entry;
//        }

        Log.d("column 0", cursor.getInt(0) + "");
        Log.d("column 1", cursor.getInt(1) + "");
        Log.d("column 2", cursor.getLong(2) + "");
        Log.d("column 3", cursor.getString(3) + "");
        Log.d("column 4", cursor.getString(4) + "");

        entry.setDuration(cursor.getInt(1));
        entry.setDateTime(cursor.getLong(2));

        String difficultyString = cursor.getString(3);
        if(!difficultyString.contains("null")){
            String[] s = difficultyString.substring(1, difficultyString.length() - 1).split(", ");
            int[] numbers = new int[s.length];
            for (int curr = 0; curr < s.length; curr++)
                numbers[curr] = Integer.parseInt(s[curr]);
            entry.setFeedBackList(numbers);
        }

        String feedbackString = cursor.getString(4);
        if (!feedbackString.contains("null")) {
            String[] s1 = feedbackString.substring(1, feedbackString.length() - 1).split(", ");
            int[] numbers1 = new int[s1.length];
            for (int curr = 0; curr < s1.length; curr++)
                numbers1[curr] = Integer.parseInt(s1[curr]);
            entry.setExerciseIdList(numbers1);
        }
        return entry;
     }

    private AbLog cursorToAblog(Cursor cursor) {
//        Log.d("column 0", cursor.getInt(0) + "");
//        Log.d("column 1", cursor.getInt(1) + "");
//        Log.d("column 2", cursor.getInt(2) + "");
//        Log.d("column 3", cursor.getString(3) + "");
//        Log.d("column 4", cursor.getString(4) + "");
//        Log.d("column 5", cursor.getString(5) + "");
        AbLog abLog = new AbLog();
        abLog.setId(cursor.getInt(0));
        abLog.setAblogNumber(cursor.getInt(1));
        abLog.setMuscleGroup(cursor.getInt(2));
        String difficultyString = cursor.getString(3);
        String[] s = difficultyString.substring(1, difficultyString.length() - 1).split(",");
        int[] numbers = new int[s.length];
        for (int curr = 0; curr < s.length; curr++) {
            Log.d("Tag", s[curr]);
            numbers[curr] = Integer.parseInt(s[curr]);
            abLog.setDifficultyArray(numbers);
            abLog.setName(cursor.getString(4));
            abLog.setFilePath(cursor.getString(5));
        }
        return abLog;
    }
}
