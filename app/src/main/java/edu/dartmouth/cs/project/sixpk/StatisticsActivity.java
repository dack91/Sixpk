package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;


public class StatisticsActivity extends Activity {
    private Button backButton;
    private TextView mRectusLevel;
    private TextView mObliqueLevel;
    private TextView mTransverse;

    private WorkoutEntryDataSource dbHelper;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        mContext = this;

        // Get references to widgets
        backButton = (Button) findViewById(R.id.backHistoryButton);
        mRectusLevel = (TextView) findViewById(R.id.textViewStatsLevelRectus);
        mObliqueLevel = (TextView) findViewById(R.id.textViewStatsLevelOblique);
        mTransverse = (TextView) findViewById(R.id.textViewStatsLevelTransverse);

        // Open database
        dbHelper = new WorkoutEntryDataSource(mContext);
        dbHelper.open();

        ArrayList<AbLog> logs = dbHelper.fetchAbLogEntries();
        for (AbLog log : logs) {
            Log.d("DEBUG", "log: " + log.getName());
            for (int j : log.getDifficultyArray())
                Log.d("DEBUG", "log difficulty: " + j);
        }

        //TODO
        // get/calculate level for each muscle group + get progress towards next level for
        // display on the drawing

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
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
