package edu.dartmouth.cs.project.sixpk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_WORKOUT_ID = "workout_id";
    public static final String COLUMN_WORKOUT_DURATION = "workout_duration";
    public static final String COLUMN_WORKOUT_DATE_TIME = "workout_date_time";
    public static final String COLUMN_WORKOUT_FEEDBACK = "workout_feedback";
    public static final String COLUMN_WORKOUT_EXERCISE_LIST = "exercise_list";

    public static final String TABLE_ABLOG = "ablogs";
    public static final String COLUMN_ABLOG_ID = "ablog_id";
    public static final String COLUMN_ABLOG_WORKOUT_ID = "ablog_workout_id";
    public static final String COLUMN_ABLOG_DIFFICULTY = "ablog_difficulty";
    public static final String COLUMN_ABLOG_MUSCLE_GROUP = "ablog_muscle_group";

	private static final String DATABASE_NAME = "sixpk.db";
	private static final int DATABASE_VERSION = 1;

  private static final String WORKOUT_DATABASE_CREATE = "create table "
			+ TABLE_WORKOUTS + "(" + COLUMN_WORKOUT_ID + " integer primary key autoincrement, "
      + COLUMN_WORKOUT_DURATION + " INTEGER NOT NULL, " + COLUMN_WORKOUT_DATE_TIME + " DATE NOT NULL, "
      + COLUMN_WORKOUT_FEEDBACK + " BLOB NOT NULL, " + COLUMN_WORKOUT_EXERCISE_LIST + " BLOB NOT NULL);";

  private static final String ABLOG_DATABASE_CREATE = "create table "
    + TABLE_ABLOG + "(" + COLUMN_ABLOG_ID + " integer primary key autoincrement, "
    + COLUMN_ABLOG_WORKOUT_ID + " INTEGER NOT NULL, " + COLUMN_ABLOG_DIFFICULTY + " BLOB NOT NULL, "
    + COLUMN_ABLOG_MUSCLE_GROUP + " INTEGER NOT NULL);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
    database.execSQL(WORKOUT_DATABASE_CREATE);
    database.execSQL(ABLOG_DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABLOG);
		onCreate(db);
	}
}