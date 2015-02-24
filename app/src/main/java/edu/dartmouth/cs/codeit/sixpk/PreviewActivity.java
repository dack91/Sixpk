package edu.dartmouth.cs.codeit.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class PreviewActivity extends Activity{//extends ListActivity {
    private EditText mDuration;
    private EditText mDifficulty;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mContext = this;

        // Get text references
        mDuration = (EditText) findViewById(R.id.editTextPrevItinerary);
        mDifficulty = (EditText) findViewById(R.id.editTextPrevDifficulty);

        // Unpack intent
        int difficulty = getIntent().getIntExtra(Globals.WORKOUT_DIFFICULTY_KEY, Globals.WORKOUT_MED);
        String diff = "MEDIUM"; // DEFAULT: medium

        switch (difficulty) {
            case Globals.WORKOUT_EASY:
                diff = "EASY";
                break;
            case Globals.WORKOUT_MED:
                diff = "MEDIUM";
                break;
            case Globals.WORKOUT_HARD:
                diff = "HARD";
                break;
        }

        int time = getIntent().getIntExtra(Globals.WORKOUT_DURATION_KEY, Globals.DEFAULT_TIME);

        // Set text views
        mDuration.setText(time + " min");
        mDifficulty.setText(diff);

        //TODO
        // Set up a listview element in the centerish of the view to scroll through
        // workout itinerary
    }

    public void onStartClicked(View v) {
        Intent i = new Intent(this, WorkoutActivity.class);
        startActivity(i);
    }

    public void onCancelClicked(View v) {
        finish();
    }

    // HARD CODED PLACEHOLD: for the listview that will show workouts in current itinerary
    public void onSampleClicked(View v) {
        Toast.makeText(this, "Show ab workout help dialog", Toast.LENGTH_SHORT).show();
    }
}
