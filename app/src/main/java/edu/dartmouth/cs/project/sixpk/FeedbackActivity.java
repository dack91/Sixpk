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
import android.widget.TextView;

import java.util.ArrayList;


public class FeedbackActivity extends Activity {
    private Context mContext;
    private ListView mFeedbackList;
    private FeedbackListAdapter mAdapter;    // implement to take a list of workouts and format display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mContext = this;

        // Get reference to listview in feedback layout
        mFeedbackList = (ListView) findViewById(R.id.listViewFeedback);

        // Set adapter for custom listView
        mAdapter = new FeedbackListAdapter(mContext);
        mFeedbackList.setAdapter(mAdapter);
        mAdapter.addAll(Globals.test);
        mAdapter.notifyDataSetChanged();
    }

    public void onSaveClicked(View v) {
        // Save workout with feedback to database
        // TODO

        // Return to home screen
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView != null) {
                view = convertView;
            }
            else {
                view = mInflater.inflate(R.layout.list_textview_seekbar, parent, false);
            }

            // TEMPORARY: probably using a Workout object or something else in the arrayadapter than a string
        //    WorkoutObject w = getItem(position);
            String temp = getItem(position);
            ((TextView)view.findViewById(R.id.textViewWorkoutSeek)).setText(temp);

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


}
