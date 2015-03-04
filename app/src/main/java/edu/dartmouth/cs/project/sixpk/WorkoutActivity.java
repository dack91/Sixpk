package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import edu.dartmouth.cs.project.sixpk.database.Workout;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;


public class WorkoutActivity extends Activity {
    private Chronometer mTimer;
    private Button mPause;
    private boolean mPaused = false;
    private long mLastPause;
    private Workout mCurrWorkout;
    private WorkoutEntryDataSource dbHelper;
    private Context mContext;
    private long mWorkoutID;
    private int[] mExerciseList;
    private int[] mDurationList;
    private int mCounter = 0;   // keep track of how many workouts were completed



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        mContext = this;


        dbHelper = new WorkoutEntryDataSource(mContext);
        dbHelper.open();

        // Setup the stopwatch for the workout
        mTimer = (Chronometer) findViewById(R.id.chronometer);
        mTimer.setBase(SystemClock.elapsedRealtime());  // start stopwatch at 0
        mTimer.start();

        // Get reference to pause stopwatch button
        mPause = (Button) findViewById(R.id.buttonPause);

        // Get current workout
        mWorkoutID = getIntent().getIntExtra(Globals.WORKOUT_ID_KEY, 0);
        mCurrWorkout = dbHelper.fetchWorkoutByIndex(mWorkoutID);
        mExerciseList = mCurrWorkout.getExerciseIdList();
        mDurationList = mCurrWorkout.getDurationList();
    }

    // Saving all exercises completed so far, go to feedback
    public void onEndEarlyClicked(View v) {
        // Delete any uncompleted exercises from the Workout in the db
        // (delete all indices after mCounter)
        // TODO
        for (int i = mCounter; i < mExerciseList.length; i++) {
            mCurrWorkout.removeExercise(i);
        }

        // Go to feedback
        Intent i = new Intent(this, FeedbackActivity.class);
        i.putExtra(Globals.WORKOUT_ID_KEY, mWorkoutID);
        startActivity(i);
    }

    public void onCancelWorkoutClicked(View v) {
        // Remove entire workout from db
        dbHelper.removeWorkoutEntry(mWorkoutID);

        // Current returns to pre-workout itinerary
        finish();
    }

    public void onPauseClicked(View v) {
        // Resume Stopwatch
        if(mPaused){
            // resume stopwatch starting from the time when it was paused
            mTimer.setBase(mTimer.getBase() + SystemClock.elapsedRealtime() - mLastPause);
            mTimer.start();
            mPause.setText(getResources().getString(R.string.pauseButtonText));
            mPaused = !mPaused;
        }
        // Pause Stopwatch
        else {
            mTimer.stop();
            mLastPause = SystemClock.elapsedRealtime(); // Save current stopwatch time
            mPause.setText(getResources().getString(R.string.resumeButtonText));
            mPaused = !mPaused;
        }
    }

    @Override
    protected void onPause() {
        dbHelper.close();
        super.onPause();
    }
}
