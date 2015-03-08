package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.Workout;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;


public class FeedbackActivity extends Activity {
    private Context mContext;
    private ListView mFeedbackList;
    private FeedbackListAdapter mAdapter;    // implement to take a list of workouts and format display
    private WorkoutEntryDataSource dbHelper;
    private long mWorkoutID;
    private Workout mCurrWorkout;
    ArrayList<String> mCompletedExercises;
    private ArrayList<Integer> feedbackArrayList = new ArrayList<>();
    private int[] feebackArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mContext = this;

        // Open database
        dbHelper = new WorkoutEntryDataSource(mContext);
        dbHelper.open();

        // Get reference to listview in feedback layout
        mFeedbackList = (ListView) findViewById(R.id.listViewFeedback);

        // Set adapter for custom listView
        mAdapter = new FeedbackListAdapter(mContext);

        // Get current workout
        mWorkoutID = getIntent().getLongExtra(Globals.WORKOUT_ID_KEY, 0);
        mCurrWorkout = dbHelper.fetchWorkoutByIndex(mWorkoutID);
        updateListView();
        mFeedbackList.setAdapter(mAdapter);

    }

    // From int[] of completed exercises, form Array of formatted workout strings
    public void updateListView() {
        mAdapter.clear();   // reset list

        final int[] exerciseList = mCurrWorkout.getExerciseIdList();
        int[] durationList = mCurrWorkout.getDurationList();

        mCompletedExercises = new ArrayList<String>();

        // Populate listview
        for (int i = 0; i < exerciseList.length; i++) {
            feedbackArrayList.add(5);
            mCompletedExercises.add(dbHelper.getNameById(exerciseList[i]) + ", " + Globals.formatDuration(durationList[i]));
        }

        mAdapter.addAll(mCompletedExercises);
    }

    public void onSaveClicked(View v) {
        // Save feedback to database
        feebackArray = convertToIntArray(feedbackArrayList);
        dbHelper.open();
        Workout workout = dbHelper.fetchWorkoutByIndex(mWorkoutID);
        workout.setFeedBackList(feebackArray);
        dbHelper.updateWorkoutEntry(mWorkoutID, workout);
        dbHelper.close();
        Intent toHomeScreen = new Intent(this, MainActivity.class);
        startActivity(toHomeScreen);
    }

    private int[] convertToIntArray(ArrayList<Integer> al) {
        ArrayList<Integer> test = al;
        int[] new_list = new int[al.size()];
        for (int i = 0; i < new_list.length; i++) {
            new_list[i] = al.get(i).intValue();
        }
        return new_list;
    }

    public void onDeleteCLicked(View v) {
        // Ask user through dialog if they really want to quit without saving
        showDeleteDialog();

    }

    // Set up adapter for listview element to scroll through workout itinerary
    private class FeedbackListAdapter extends ArrayAdapter<String> {
        private final LayoutInflater mInflater;

        public FeedbackListAdapter(Context context) {
            super(context, R.layout.list_textview_seekbar);
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView != null) {
                view = convertView;
            }
            else {
                view = mInflater.inflate(R.layout.list_textview_seekbar, parent, false);
            }

            SeekBar seekbar = (SeekBar) view.findViewById(R.id.seekBar1);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    feedbackArrayList.set(position, (progress/10));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            String text = getItem(position);
            ((TextView)view.findViewById(R.id.textViewWorkoutSeek)).setText(text);

            return view;
        }
    }

    // Show Dialog fragment to demo of workout from listView click
    public void showDeleteDialog() {
        DialogFragment newFragment = FeedbackDeleteFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");
    }

    // Dialog to make sure the user wants to quit the completed workout without saving
    public static class FeedbackDeleteFragment extends DialogFragment {

        public static FeedbackDeleteFragment newInstance() {
            FeedbackDeleteFragment frag = new FeedbackDeleteFragment();
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.logo1)  // will need to get image associated with passed position
                    .setTitle(R.string.feedback_dialog_delete)
                    .setPositiveButton(R.string.feedback_dialog_no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dismiss();  // close dialog
                                }
                            })
                    .setNegativeButton(R.string.feedback_dialog_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((FeedbackActivity) getActivity())
                                            .doNegativeClick();
                                }
                            }).create();
        }
    }

    private void doNegativeClick() {
        // Return to start without saving workout
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        dbHelper.close();
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.open();
    }
}
