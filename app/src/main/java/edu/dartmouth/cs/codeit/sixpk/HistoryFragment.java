package edu.dartmouth.cs.codeit.sixpk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view in the start xml
        return inflater.inflate(R.layout.fragment_history, container, false);


        // Show listview of past workouts
        // TODO
    }


    public void onShowStatsClicked(View v) {
        // Go to statisticsActivity to see data visualization of workout history
        Intent i = new Intent(getActivity(), StatisticsActivity.class);
        startActivity(i);
    }


}
