package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
    private TextView mCurrExerciseDurationText;

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
        mCurrExerciseDurationText = (TextView) findViewById(R.id.workoutCurrExerciseEndTimeNumber);

        // Get current workout
        mWorkoutID = getIntent().getLongExtra(Globals.WORKOUT_ID_KEY, 0);
        mCurrWorkout = dbHelper.fetchWorkoutByIndex(mWorkoutID);
        mExerciseList = mCurrWorkout.getExerciseIdList();
        mDurationList = mCurrWorkout.getDurationList();
        TOTAL_TIME = mCurrWorkout.getDuration();

        // Set initial values for textViews and imageViews.
        mCurrExerciseImage.setImageResource(getExerciseImage(mCurrExercise));
        mCurrExerciseText.setText(dbHelper.getNameById(mExerciseList[mCurrExercise]));
        mCurrExerciseDurationText.setText(Globals.formatTime(mDurationList[mCurrExercise]));

        // Check there is more than one exercise
        if (mExerciseList.length > 1) {
            mNextExerciseImage.setImageResource(getExerciseImage(mCurrExercise + 1));
            mNextExerciseText.setText(dbHelper.getNameById(mExerciseList[mCurrExercise + 1]));
        }
        else {
            mNextExerciseText.setText("");
            mNextExerciseImage.setImageResource(0); // no image
        }

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

    // Update current and next exercise
    // play beep sound at change
    public void nextExercise() {
        final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        tg.startTone(ToneGenerator.TONE_PROP_ACK);
        mCurrExerciseImage.setImageResource(getExerciseImage(mCurrExercise));
        mCurrExerciseText.setText(dbHelper.getNameById(mExerciseList[mCurrExercise]));
        mCurrExerciseDurationText.setText(Globals.formatTime(mTimeSoFar + mDurationList[mCurrExercise]));

        // Check if current exercise is last in set
        if (mCurrExercise < mExerciseList.length - 1) {
            mNextExerciseImage.setImageResource(getExerciseImage(mCurrExercise + 1));
            mNextExerciseText.setText(dbHelper.getNameById(mExerciseList[mCurrExercise + 1]));

        }
        // Last exercise is current
        else {
            mNextExerciseText.setText("");
            mNextExerciseImage.setImageResource(0); // no image
        }
    }

    // Return resource drawable id for given exercise id
    public int getExerciseImage(int index) {
        return getResources().getIdentifier("a" + mExerciseList[index], "drawable", getPackageName());

    }

    // Saving all exercises completed so far, go to feedback
    public void onEndEarlyClicked(View v) {
        // No exercises completed
        if (mCounter == 0) {
            // Remove entire workout from db and return to start
            dbHelper.removeWorkoutEntry(mWorkoutID);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        // Some exercise but not all completed
        else {

            // Delete any uncompleted exercises from the Workout in the db
            // (delete all indices after mCounter)
            mCurrWorkout.removeRemainingExercises(mCounter);

            // Update database
            dbHelper.updateWorkoutEntry(mWorkoutID, mCurrWorkout);

            // Go to feedback
            workoutCompleted();
        }
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
    public void onPause() {
        dbHelper.close();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        dbHelper.open();
    }
}
