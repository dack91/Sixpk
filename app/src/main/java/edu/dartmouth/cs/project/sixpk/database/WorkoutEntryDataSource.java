package edu.dartmouth.cs.project.sixpk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Date;

public class WorkoutEntryDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allWorkoutColumns = {MySQLiteHelper.COLUMN_WORKOUT_ID,
    MySQLiteHelper.COLUMN_WORKOUT_DURATION, MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME,
    MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST};
  private String[] allAblogColumns = {MySQLiteHelper.COLUMN_ABLOG_ID,
    MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP,
    MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY};

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
  public long insertWorkoutEntry(WorkoutEntry entry) {

    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_WORKOUT_ID, entry.getId());
    values.put(MySQLiteHelper.COLUMN_WORKOUT_DATE_TIME, String.valueOf(entry.getDateTime()));
    values.put(MySQLiteHelper.COLUMN_WORKOUT_DURATION, entry.getDuration());
    values.put(MySQLiteHelper.COLUMN_WORKOUT_FEEDBACK, entry.getFeedBackList().toString());
    values.put(MySQLiteHelper.COLUMN_WORKOUT_EXERCISE_LIST, entry.getExerciseIdList().toString());

    long insertId = database.insert(MySQLiteHelper.TABLE_WORKOUTS, null, values);

    return insertId;
  }

  // Insert a item given each column value
  public long insertAblogEntry(AbLog ablog) {

    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_ABLOG_ID, ablog.getId());
    values.put(MySQLiteHelper.COLUMN_ABLOG_WORKOUT_ID, ablog.getAblogId());
    values.put(MySQLiteHelper.COLUMN_ABLOG_DIFFICULTY, ablog.getDifficultyArray().toString());
    values.put(MySQLiteHelper.COLUMN_ABLOG_MUSCLE_GROUP, ablog.getMuscleGroup());

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
  public WorkoutEntry fetchWorkoutEntryByIndex(long rowId) {
    String mId = String.valueOf(rowId);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS, allWorkoutColumns, "?=?", new String[]{MySQLiteHelper.COLUMN_WORKOUT_ID, mId}, null, null, null);
    cursor.moveToFirst();
    return cursorToWorkoutEntry(cursor);
  }

  // Query a specific entry by its index.
  public AbLog fetchAbLogByIndex(long rowId) {
    String mId = String.valueOf(rowId);
    Cursor cursor = database.query(MySQLiteHelper.TABLE_ABLOG, allAblogColumns, "?=?", new String[]{MySQLiteHelper.COLUMN_ABLOG_ID, mId}, null, null, null);
    cursor.moveToFirst();
    return cursorToAblog(cursor);
  }

  // Query the entire table, return all rows
  public ArrayList<WorkoutEntry> fetchWorkoutEntries() {
    ArrayList<WorkoutEntry> entries = new ArrayList<WorkoutEntry>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_WORKOUTS,
      allWorkoutColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      WorkoutEntry mEntry = cursorToWorkoutEntry(cursor);
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
    // make sure to close the cursor
    cursor.close();
    return entries;
  }

  private WorkoutEntry cursorToWorkoutEntry(Cursor cursor) {
    WorkoutEntry entry = new WorkoutEntry();
    entry.setDateTime(new Date(cursor.getInt(0)));
    entry.setDifficulty(cursor.getInt(1));
    entry.setDuration(cursor.getInt(2));

    String[] s = cursor.getString(3).split(",");
    int[] numbers = new int[s.length];
    for (int curr = 0; curr < s.length; curr++)
      numbers[curr] = Integer.parseInt(s[curr]);
    entry.setExerciseIdList(numbers);

    String[] s1 = cursor.getString(4).split(",");
    float[] numbers1 = new float[s.length];
    for (int curr = 0; curr < s.length; curr++)
      numbers1[curr] = Float.parseFloat(s1[curr]);
    entry.setFeedBackList(numbers1);
    return entry;
  }

//  // Convert byte array to Location ArrayList
//  public ArrayList<WorkoutEntry> setLocationListFromByteArray(byte[] bytePointArray) {
//
//    ByteBuffer byteBuffer = ByteBuffer.wrap(bytePointArray);
//    IntBuffer intBuffer = byteBuffer.asIntBuffer();
//
//    int[] intArray = new int[bytePointArray.length / Integer.SIZE];
//    intBuffer.get(intArray);
//
//    int locationNum = intArray.length / 2;
//
//    ArrayList<WorkoutEntry> mLocationLatLngList = new ArrayList<>();
//
//    for (int i = 0; i < locationNum; i++) {
//       latLng = new LatLng((double) intArray[i * 2] / 1E6F,
//        (double) intArray[i * 2 + 1] / 1E6F);
//      mLocationLatLngList.add(latLng);
//    }
//    return mLocationLatLngList;
//  }

  private AbLog cursorToAblog(Cursor cursor){
    AbLog abLog = new AbLog();
    abLog.setAblogId(cursor.getInt(0));
    abLog.setAblogId(cursor.getInt(1));
    abLog.setMuscleGroup(cursor.getInt(2));
    String[] s = cursor.getString(3).split(",");
    int[] numbers = new int[s.length];
    for (int curr = 0; curr < s.length; curr++)
      numbers[curr] = Integer.parseInt(s[curr]);
    abLog.setDifficultyArray(numbers);
    return abLog;
  }

}
