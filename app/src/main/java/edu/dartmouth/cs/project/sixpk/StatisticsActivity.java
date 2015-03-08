package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StatisticsActivity extends Activity {
    private Button backButton;
    private TextView mRectusLevel;
    private TextView mObliqueLevel;
    private TextView mTransverse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        backButton = (Button) findViewById(R.id.backHistoryButton);
        mRectusLevel = (TextView) findViewById(R.id.textViewStatsLevelRectus);
        mObliqueLevel = (TextView) findViewById(R.id.textViewStatsLevelOblique);
        mTransverse = (TextView) findViewById(R.id.textViewStatsLevelTransverse);

        //TODO
        // get/calculate level for each muscle group + get progress towards next level for
        // display on the drawing

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
