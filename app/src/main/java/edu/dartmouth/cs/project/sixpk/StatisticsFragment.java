package edu.dartmouth.cs.project.sixpk;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.util.Random;


public class StatisticsFragment extends Fragment {
    // Personal levels for total app and each muscle group
    private TextView mRectusLevel;
    private TextView mObliqueLevel;
    private TextView mTransverseLevel;
    private TextView mTotalLevel;

    // Display progress of each muscle group towards next level
    private ImageView mRectusImage;
    private ImageView mObliquesImage;
    private ImageView mTransverseImage;

    private Context mContext;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view in the start xml
        View view = inflater.inflate(R.layout.activity_statistics, container, false);
        mContext = getActivity();

        // setContentView(R.layout.activity_statistics);
        prefs = mContext.getSharedPreferences(getString(R.string.prefs_key), mContext.MODE_PRIVATE);

        // Get references to widgets
        mRectusLevel = (TextView) view.findViewById(R.id.textViewStatsLevelRectus);
        mObliqueLevel = (TextView) view.findViewById(R.id.textViewStatsLevelOblique);
        mTransverseLevel = (TextView) view.findViewById(R.id.textViewStatsLevelTransverse);
        mRectusImage = (ImageView) view.findViewById(R.id.imageViewRectus);
        mObliquesImage = (ImageView) view.findViewById(R.id.imageViewObliques);
        mTransverseImage = (ImageView) view.findViewById(R.id.imageViewTransverse);
        mTotalLevel = (TextView) view.findViewById(R.id.textViewStatsLevelsTotal);

        // Set text and images to appropriate level and progress
        int rect = prefs.getInt(getString(R.string.rectusStatLevel), 0);
        int obliq = prefs.getInt(getString(R.string.obliquesStatLevel), 0);
        int transv = prefs.getInt(getString(R.string.transverseStatLevel), 0);
        int rectProg = prefs.getInt(getString(R.string.rectusStatProgress), 0);
        int obliqProg = prefs.getInt(getString(R.string.obliquesStatProgress), 0);
        int transvProg = prefs.getInt(getString(R.string.transverseStatProgress), 0);

        // Muscle group text
        mRectusLevel.setText(Integer.toString(rect));
        mObliqueLevel.setText(Integer.toString(obliq));
        mTransverseLevel.setText(Integer.toString(transv));

        // Overall level text
        int totalLevel = (rect + obliq + transv) / 3;
        mTotalLevel.setText(Integer.toString(totalLevel));

        // Calculate progress towards next level
        int rProgress = getProgress(rect, rectProg);
        int oProgress = getProgress(obliq, obliqProg);
        int tProgress = getProgress(transv, transvProg);

        // Display progress towards next level
        mRectusImage.setImageResource(getProgressImage("rectus", rProgress));
        mObliquesImage.setImageResource(getProgressImage("obliques", oProgress));
        mTransverseImage.setImageResource(getProgressImage("transverse", tProgress));

        return view;
    }

    // Return 25, 50 or 75 based on current progress through level
    // Higher levels take more progress to move up
    public int getProgress(int level, int progress) {
        int p;
        int nextLevel = level * Globals.LEVEL_INCREMENTER;
        double curr = ((double) progress / (double) nextLevel) * 100;

        // Less than one third of the way towards next level
        if (curr <= 33)
            p = 25;
        // Less than two thirds of the way towards next level
        else if (curr <= 66)
            p = 50;
        // Less than full towards next level
        else
            p = 75;
        return p;
    }

    // Return resource drawable id for given exercise id
    public int getProgressImage(String group, int prog) {
        return getResources().getIdentifier("" + group + prog, "drawable", mContext.getPackageName());

    }

    public void sendMessage() {
        Random r = new Random();
        int rand = r.nextInt(Globals.Stats_Toast.size());
        Toast.makeText(mContext, Globals.Stats_Toast.get(rand), Toast.LENGTH_SHORT).show();
    }
}