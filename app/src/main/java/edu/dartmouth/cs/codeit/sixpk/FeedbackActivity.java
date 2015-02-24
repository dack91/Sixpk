package edu.dartmouth.cs.codeit.sixpk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class FeedbackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    public void onSaveClicked(View v) {
        // Save workout to database
        // TODO

        // Return to home screen
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onDeleteCLicked(View v) {
        // Ask user through dialog if they really want to quit without saving
        // TODO

        // Return to start without saving workout
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
