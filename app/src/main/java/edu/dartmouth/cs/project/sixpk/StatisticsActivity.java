package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;


public class StatisticsActivity extends Activity {
    private Button backButton;
    private TextView mRectusLevel;
    private TextView mObliqueLevel;
    private TextView mTransverseLevel;

    private ImageView mRectusImage;
    private ImageView mObliquesImage;
    private ImageView mTransverseImage;

//    private WorkoutEntryDataSource dbHelper;
//    private Context mContext;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
//        mContext = this;
        prefs = getSharedPreferences(getString(R.string.prefs_key), MODE_PRIVATE);

        // Get references to widgets
        backButton = (Button) findViewById(R.id.backHistoryButton);
        mRectusLevel = (TextView) findViewById(R.id.textViewStatsLevelRectus);
        mObliqueLevel = (TextView) findViewById(R.id.textViewStatsLevelOblique);
        mTransverseLevel = (TextView) findViewById(R.id.textViewStatsLevelTransverse);
        mRectusImage = (ImageView) findViewById(R.id.imageViewRectus);
        mObliquesImage = (ImageView) findViewById(R.id.imageViewObliques);
        mTransverseImage = (ImageView) findViewById(R.id.imageViewTransverse);

        // Set text and images to appropriate level and progress
        int rect = prefs.getInt(getString(R.string.rectusStatLevel), 0);
        int obliq = prefs.getInt(getString(R.string.obliquesStatLevel), 0);
        int transv = prefs.getInt(getString(R.string.transverseStatLevel), 0);
        int rectProg = prefs.getInt(getString(R.string.rectusStatProgress), 0);
        int obliqProg = prefs.getInt(getString(R.string.obliquesStatProgress), 0);
        int transvProg = prefs.getInt(getString(R.string.transverseStatProgress), 0);

        Log.d("DEBUG", "Level/progress:\n" + rect + "/" + rectProg + "\n" + obliq + "/" + obliqProg + "\n" + transv
                + "/" + transvProg);


        mRectusLevel.setText(Integer.toString(rect));
        mObliqueLevel.setText(Integer.toString(obliq));
        mTransverseLevel.setText(Integer.toString(transv));

        int rProgress = getProgress(rect, rectProg);
        int oProgress = getProgress(obliq, obliqProg);
        int tProgress = getProgress(transv, transvProg);

        mRectusImage.setImageResource(getProgressImage("rectus", rProgress));
        mObliquesImage.setImageResource(getProgressImage("obliques", oProgress));
        mTransverseImage.setImageResource(getProgressImage("transverse", tProgress));

//        // Open database
//        dbHelper = new WorkoutEntryDataSource(mContext);
//        dbHelper.open();
//
//        ArrayList<AbLog> logs = dbHelper.fetchAbLogEntries();
//        for (AbLog log : logs) {
//            Log.d("DEBUG", "log: " + log.getName());
//            for (int j : log.getDifficultyArray())
//                Log.d("DEBUG", "difficulty: " + j);
//        }

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    // return 25, 50 or 75 based on current progress through level
    // higher level takes more progress to move up
    public int getProgress(int level, int progress) {
        int p;
        int nextLevel = level * Globals.LEVEL_INCREMENTER;
        double curr = ((double) progress / (double) nextLevel) * 100;
        Log.d("DEBUG", "next level: " + nextLevel);
        Log.d("DEBUG", "progress: " + progress);
        Log.d("DEBUG", "current progress: " + curr);


        if (curr <= 33)
            p = 25;
        else if (curr <= 66)
            p = 50;
        else
            p = 75;
        return p;
    }

    // Return resource drawable id for given exercise id
    public int getProgressImage(String group, int prog) {
        return getResources().getIdentifier("" + group + prog, "drawable", getPackageName());

    }

//    @Override
//    public void onPause() {
//        dbHelper.close();
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        dbHelper.open();
//    }
}
