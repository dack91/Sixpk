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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private Context mContext;
    private ListView mExerciseList;
    private HistoryListAdapter mAdapter;    // implement to take a list of workouts and format display

    private WorkoutEntryDataSource dbHelper;
    private ArrayList<AbLog> mAllExercises;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view in the start xml
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mContext = getActivity();

        dbHelper = new WorkoutEntryDataSource(mContext);
        dbHelper.open();

        // Get reference to listview in feedback layout
        mExerciseList = (ListView) view.findViewById(R.id.listViewHistory);

        // Show listview of all exercises
        // TODO
        // Set adapter for custom listView
        mAdapter = new HistoryListAdapter(mContext);
        mExerciseList.setAdapter(mAdapter);
        //   mAdapter.addAll(Globals.test);  // TEMPORARY: change to get all exercises from database
        mAllExercises = dbHelper.fetchAbLogEntries();
        mAdapter.addAll(mAllExercises);

        mAdapter.notifyDataSetChanged();

        // Set onClick listener for the listView, show dialog on click
        mExerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                AbLog log =  (AbLog) mExerciseList.getItemAtPosition(position);
                int group = log.getMuscleGroup();

                String muscleGroup = "";
                switch (group) {
                    case 1:
                        muscleGroup = "Rectus";
                        break;
                    case 2:
                        muscleGroup = "Obliques";
                        break;
                    case 3:
                        muscleGroup = "Transverse";
                        break;
                }

                String title = muscleGroup + ": " + log.getName();

                showExerciseDialog(title, ((AbLog) mExerciseList.getItemAtPosition(position)).getFilePath());
            }
        });

        return view;
    }

    private class HistoryListAdapter extends ArrayAdapter<AbLog> {
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


            //String temp = getItem(position);
            AbLog curr = getItem(position);
            ((TextView)view.findViewById(android.R.id.text1)).setText(curr.getName());

            return view;
        }
    }

    // Show Dialog fragment to demo of workout from listView click
    public void showExerciseDialog(String workoutTitle, String GIF){
        DialogFragment newFragment = ExerciseDemoFragment.newInstance(workoutTitle, GIF);
        newFragment.show(getFragmentManager(), "dialog");
    }

    public static class ExerciseDemoFragment extends DialogFragment {

        public static ExerciseDemoFragment newInstance(String title, String GIF) {
            ExerciseDemoFragment frag = new ExerciseDemoFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("gif", GIF);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            final int position = getArguments().getInt("position");
            String gif = getArguments().getString("gif");


            WebView gifView = new WebView(getActivity());
            String HTML_FORMAT = "<html><body style=\"text-align: center; background-color: null; vertical-align: middle;\"><img src = \"%s\" /></body></html>";

            final String html = String.format(HTML_FORMAT, gif);

            gifView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
            gifView.setBackgroundColor(0x00000000);

            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.logo1)
                    .setTitle(title)
                    .setView(gifView)
                    .setPositiveButton(R.string.workout_dialog_ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dismiss();
                                }
                            }).create();
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


