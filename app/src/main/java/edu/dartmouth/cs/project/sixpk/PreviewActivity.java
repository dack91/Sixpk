package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.MySQLiteHelper;
import edu.dartmouth.cs.project.sixpk.database.Workout;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;


public class PreviewActivity extends Activity {//extends ListActivity {
    private EditText mDuration;
    private EditText mDifficulty;
    private Context mContext;
    private ListView mItineraryList;
    private ItineraryListAdapter mAdapter;    // implement to take a list of workouts and format display

    private WorkoutEntryDataSource dbHelper;
    ArrayList<String> mExerciseItinerary;
    private Workout mCurrWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);
        mContext = this;

        dbHelper = new WorkoutEntryDataSource(mContext);
        dbHelper.open();

        mAdapter = new ItineraryListAdapter(mContext);

        // Get text references
        mDuration = (EditText) findViewById(R.id.editTextPrevItinerary);
        mDifficulty = (EditText) findViewById(R.id.editTextPrevDifficulty);
        mItineraryList = (ListView) findViewById(R.id.listViewPreview);

        // Unpack intent
        int difficulty = getIntent().getIntExtra(Globals.WORKOUT_DIFFICULTY_KEY, Globals.WORKOUT_MED);
        int time = getIntent().getIntExtra(Globals.WORKOUT_DURATION_KEY, Globals.DEFAULT_TIME);

        // If difficulty not set, default to medium
        if (difficulty == -1) {
            difficulty = Globals.WORKOUT_MED;
        }

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

        // Get exercises in current workout
        ArrayList<AbLog> allExercises = dbHelper.fetchAbLogEntries();
        mCurrWorkout = new Workout(allExercises, time, difficulty);
        updateListView();
        mItineraryList.setAdapter(mAdapter);

        // Set header text views
        mDuration.setText(time + " min");
        mDifficulty.setText(diff);

        // Set onClick listener for the listView, show dialog on click
        mItineraryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                String title = dbHelper.getNameById(mCurrWorkout.getExerciseIdList()[position]);
                showWorkoutDialog(title, position);
            }
        });
    }

    public void onStartClicked(View v) {
        // Save current workout to database
        long workoutID = dbHelper.insertWorkoutEntry(mCurrWorkout);

        Log.d("DEBUG", "workout id: " + workoutID);
        Log.d("DEBUG", "curr duration: " + mCurrWorkout.getDuration());

        // Start workout
        Intent i = new Intent(this, WorkoutActivity.class);
        i.putExtra(Globals.WORKOUT_ID_KEY, workoutID);
        startActivity(i);
    }

    public void onCancelClicked(View v) {
        finish();
    }

    // Set up adapter for listview element to scroll through workout itinerary
    private class ItineraryListAdapter extends ArrayAdapter<String> {
        private final LayoutInflater mInflater;

        public ItineraryListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView != null) {
                view = convertView;
            } else {
                view = mInflater.inflate(R.layout.custom_simple_list_1, parent, false);
            }

            String text = getItem(position);
            ((TextView) view.findViewById(android.R.id.text1)).setText(text);

            return view;
        }
    }

    // Show Dialog fragment to demo of workout from listView click
    public void showWorkoutDialog(String workoutTitle, int position) {
        DialogFragment newFragment = WorkoutDemoFragment.newInstance(workoutTitle, position);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public static class WorkoutDemoFragment extends DialogFragment {

        public static WorkoutDemoFragment newInstance(String title, int position) {
            WorkoutDemoFragment frag = new WorkoutDemoFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putInt("position", position);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            final int position = getArguments().getInt("position");

            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.logo1)
                    .setTitle(title)
                    .setPositiveButton(R.string.workout_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dismiss();
                                }
                            })
                    .setNegativeButton(R.string.alert_dialog_delete,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((PreviewActivity) getActivity())
                                            .doNegativeClick(position);
                                }
                            }).create();
        }
    }

    // Delete workout from itinerary
    private void doNegativeClick(int index) {
        // Delete workout from itinerary
        mCurrWorkout.removeExercise(index);
        updateListView();

        // Update UI view of list
        mAdapter.notifyDataSetChanged();
    }

    // From int[] of completed exercises, form Array of formatted workout strings
    public void updateListView() {
        mAdapter.clear();   // reset list

        final int[] exerciseList = mCurrWorkout.getExerciseIdList();
        int[] durationList = mCurrWorkout.getDurationList();

        mExerciseItinerary = new ArrayList<String>();

        // Populate listview
        for (int i = 0; i < exerciseList.length; i++) {
            mExerciseItinerary.add(dbHelper.getNameById(exerciseList[i]) + ", "
                    + Globals.formatDuration(durationList[i]));
        }
        mAdapter.addAll(mExerciseItinerary);
    }

    @Override
    public void onPause() {
        dbHelper.close();
        super.onPause();
    }
}
