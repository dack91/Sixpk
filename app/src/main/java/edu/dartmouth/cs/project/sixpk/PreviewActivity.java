package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class PreviewActivity extends Activity{//extends ListActivity {
    private EditText mDuration;
    private EditText mDifficulty;
    private Context mContext;
    private ListView mItineraryList;
  //  private ItineraryListAdapter mAdapter;    // implement to take a list of workouts and format display

    // Test list of workouts for listview -- TEMPORARY
    ArrayList<String> test = new ArrayList<String>(){{
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
        add("A workout: 3:30");
        add("B workout: 1:45");
        add("C workout: 2:30");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mContext = this;

        // Get text references
        mDuration = (EditText) findViewById(R.id.editTextPrevItinerary);
        mDifficulty = (EditText) findViewById(R.id.editTextPrevDifficulty);
        mItineraryList = (ListView) findViewById(R.id.listViewPreview);

        // Set adapter for custom listView
//        mAdapter = new ItineraryListAdapter(mContext);
//        mItineraryList.setAdapter(mAdapter);

        // Set adapter for test listView -- TEMPORARY
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, test);
        mItineraryList.setAdapter(adapter);

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
    }

    public void onStartClicked(View v) {
        Intent i = new Intent(this, WorkoutActivity.class);
        startActivity(i);
    }

    public void onCancelClicked(View v) {
        finish();
    }


    //TODO
    // Set up adapter for listview element to scroll through workout itinerary
//    private class ItineraryListAdapter extends ArrayAdapter<WorkoutObject> {
//
//    }
}
