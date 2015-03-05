package edu.dartmouth.cs.project.sixpk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private Button showStats;
    private Context mContext;
    private ListView mExerciseList;
    private HistoryListAdapter mAdapter;    // implement to take a list of workouts and format display

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view in the start xml
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mContext = getActivity();

        // Get reference to listview in feedback layout
        mExerciseList = (ListView) view.findViewById(R.id.listViewHistory);

        // Show listview of all exercises
        // TODO
        // Set adapter for custom listView
        mAdapter = new HistoryListAdapter(mContext);
        mExerciseList.setAdapter(mAdapter);
        mAdapter.addAll(Globals.test);  // TEMPORARY: change to get all exercises from database
        mAdapter.notifyDataSetChanged();

        // Show workout statistics
        showStats = (Button) view.findViewById(R.id.buttonShowStats);
        showStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go to statisticsActivity to see data visualization of workout history
                Intent i = new Intent(getActivity(), StatisticsActivity.class);
                startActivity(i);
            }
        });

        // Set onClick listener for the listView, show dialog on click
        mExerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                String title = "Exercise: " + position;  // TEMPORARY
                showExerciseDialog(title, position);
            }
        });

        return view;
    }

        private class HistoryListAdapter extends ArrayAdapter<String> {
        private final LayoutInflater mInflater;

        public HistoryListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView != null) {
                view = convertView;
            }
            else {
//                view = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                view = mInflater.inflate(R.layout.custom_simple_list_1, parent, false);
            }


          //  WorkoutObject w = getItem(position);
            String temp = getItem(position);
            ((TextView)view.findViewById(android.R.id.text1)).setText(temp);

            return view;
        }
    }

    // Show Dialog fragment to demo of workout from listView click
    public void showExerciseDialog(String workoutTitle, int position) {
        DialogFragment newFragment = ExerciseDemoFragment.newInstance(workoutTitle, position);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public static class ExerciseDemoFragment extends DialogFragment {

        public static ExerciseDemoFragment newInstance(String title, int position) {
            ExerciseDemoFragment frag = new ExerciseDemoFragment();
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
                    .setIcon(R.drawable.logo1)  // will need to get image associated with passed position
                    .setTitle(title)
                    .setPositiveButton(R.string.workout_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dismiss();
                                }
                            }).create();
        }
    }
}
