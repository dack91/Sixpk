package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int[] mExerciseList;    // list of all exercises in workouts
    private int[] mDurationList;    // parallel list of all durations for exercises

    private int mCounter = 0;       // keep track of how many workouts were completed
    private int TOTAL_TIME;         // full workout duration

    private int mCurrExercise = 0;  // exercise ID for current exercise
    private int mTimeSoFar = 0;     // increment after each exercise finishes

    // Widgets for displaying current and next exercises
    private ImageView mCurrExerciseImage;
    private TextView mCurrExerciseText;
    private ImageView mNextExerciseImage;
    private TextView mNextExerciseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        mContext = this;

        // Open database
        dbHelper = new WorkoutEntryDataSource(mContext);
        dbHelper.open();

        // Setup the stopwatch for the workout
        mTimer = (Chronometer) findViewById(R.id.chronometer);
        mTimer.setBase(SystemClock.elapsedRealtime());  // start stopwatch at 0
        mTimer.start();

        // Get reference to pause stopwatch button
        mPause = (Button) findViewById(R.id.buttonPause);

        // Get reference to current and next exercise textview and imageviews
        mCurrExerciseImage = (ImageView) findViewById(R.id.imageViewCurrExercise);
        mCurrExerciseText = (TextView) findViewById(R.id.workoutCurrExercise);
        mNextExerciseImage = (ImageView) findViewById(R.id.imageViewNextExercise);
        mNextExerciseText = (TextView) findViewById(R.id.workoutNextExercise);

        // Get current workout
        mWorkoutID = getIntent().getIntExtra(Globals.WORKOUT_ID_KEY, 0);
        mCurrWorkout = dbHelper.fetchWorkoutByIndex(mWorkoutID);
        mExerciseList = mCurrWorkout.getExerciseIdList();
        mDurationList = mCurrWorkout.getDurationList();
        TOTAL_TIME = mCurrWorkout.getDuration();

        // Set up timer listener to change exercise/finish workout
        mTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
        {
            // Check to see if exercises should be updated
            @Override
            public void onChronometerTick(Chronometer chronometer)
            {
                // Get current elapsed time
                long currTime = (int) (SystemClock.elapsedRealtime() - mTimer.getBase()) / 1000;

                // Last exercise completed
                if (currTime == TOTAL_TIME) {
                    workoutCompleted();
                }

                // Update current and next exercise
                else if (currTime == (mTimeSoFar + mDurationList[mCurrExercise])) {
                    mTimeSoFar += mDurationList[mCurrExercise];
                    mCurrExercise++;
                    mCounter++;
                    nextExercise();
                }
            }
        });

    }

    //TODO
    // Update current and next exercise
    // play beep sound?
    public void nextExercise() {
        Log.d("DEBUG", "Updating to curr exercise: " + mExerciseList[mCurrExercise]);
   //     mCurrExerciseImage.setImageURI(getExerciseImage(mCurrExercise));
        mCurrExerciseText.setText(getExerciseText(mCurrExercise));

        if (mCurrExercise < mExerciseList.length) {
   //         mNextExerciseImage.setImageURI(getExerciseImage(mCurrExercise + 1));
            mNextExerciseText.setText(getExerciseText(mCurrExercise + 1));
        }
    }

    public String getExerciseText(int index) {
        String name = "";

        // TODO
        // get the exercise name at given index

        return name;
    }

//    public Uri getExerciseImage(int index) {
//        // TODO
//        // get the exercise image at given index
//        return new Uri();
//    }

    // Saving all exercises completed so far, go to feedback
    public void onEndEarlyClicked(View v) {
        // Delete any uncompleted exercises from the Workout in the db
        // (delete all indices after mCounter)
        for (int i = mCounter; i < mExerciseList.length; i++) {
            mCurrWorkout.removeExercise(i);
        }

        // Go to feedback
        workoutCompleted();
    }

    public void workoutCompleted() {
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
