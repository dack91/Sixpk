package edu.dartmouth.cs.codeit.sixpk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;


public class WorkoutActivity extends Activity {
    private Chronometer mTimer;
    private Button mPause;
    private boolean mPaused = false;
    private long mLastPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Setup the stopwatch for the workout
        mTimer = (Chronometer) findViewById(R.id.chronometer);
        mTimer.setBase(SystemClock.elapsedRealtime());  // start stopwatch at 0
        mTimer.start();

        // Get reference to pause stopwatch button
        mPause = (Button) findViewById(R.id.buttonPause);
    }


    public void onEndEarlyClicked(View v) {
        // Save progress through workout so far
        // TODO

        // Go to feedback
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }
    public void onCancelWorkoutClicked(View v) {
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
}
