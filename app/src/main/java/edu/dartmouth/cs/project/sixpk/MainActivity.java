package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.InitialAbInputs;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;
import edu.dartmouth.cs.project.sixpk.view.SlidingTabLayout;


public class MainActivity extends Activity {
    // Private objects to handle the sliding tab layout
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define SlidingTabLayout (shown at top)
        // and ViewPager (shown at bottom) in the layout.
        // Get their instances.
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // create a fragment list in order.
        fragments = new ArrayList<Fragment>();
        fragments.add(new StartFragment());
        fragments.add(new ExerciseFragment());
        fragments.add(new StatisticsFragment());

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles) and
        // ViewPager (different pages of fragment) together.
        myViewPageAdapter =new ActionTabsViewPagerAdapter(getFragmentManager(),
                fragments);

        // Set tab slider color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white_opaque);
            }
        });
        viewPager.setAdapter(myViewPageAdapter);

        // make sure the tabs are equally spaced.
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        // Track which fragment is in focus
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position)
            {
                // Trash if statisticsFragment is in focus
                if (position == 2) {
                    StatisticsFragment statsFragment = (StatisticsFragment) fragments.get(2);
                    // Display encouraging toast!!
                    statsFragment.sendMessage();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }
        });

        // shared preferences to store a boolean for whether or not to load all ablogs into database
        String mKey = getString(R.string.prefs_key);
        prefs = getSharedPreferences(mKey, MODE_PRIVATE);

        // runs once, the first time the app is loaded
        // need to uninstall and reinstall app if database columns are changed
        if (prefs.getBoolean(getString(R.string.initial_input), true)) {

            WorkoutEntryDataSource dbHelper = new WorkoutEntryDataSource(this);
            dbHelper.open();

            // i will become the abLogNumber
            int i = 0;
            for (int j = 0; j < InitialAbInputs.rectusArray.length; j++) {
                dbHelper.insertAblogEntry(new AbLog(InitialAbInputs.rectusArray[j],
                        i, InitialAbInputs.RECTUS, InitialAbInputs.rectusGIFArray[j]));
                Globals.ALL_EXERCISES.add(InitialAbInputs.rectusArray[j]);
                i++;
            }
            for (int j = 0; j < InitialAbInputs.obliqueArray.length; j++) {
                dbHelper.insertAblogEntry(new AbLog(InitialAbInputs.obliqueArray[j],
                        i, InitialAbInputs.OBLIQUES, InitialAbInputs.obliqueGIFArray[j]));
                Globals.ALL_EXERCISES.add(InitialAbInputs.obliqueArray[j]);
                i++;
            }
            for (int j = 0; j < InitialAbInputs.tranverseArray.length; j++) {
                dbHelper.insertAblogEntry(new AbLog(InitialAbInputs.tranverseArray[j],
                        i, InitialAbInputs.TRANSVERSE, InitialAbInputs.transverseGIFArray[j]));
                Globals.ALL_EXERCISES.add(InitialAbInputs.tranverseArray[j]);
                i++;
            }

            dbHelper.close();

            // initialize levels for statistics page
            SharedPreferences.Editor editor = prefs.edit();
            // Initial input
            editor.putBoolean(getString(R.string.initial_input), false);

            // Initial levels and level progress
            editor.putInt(getString(R.string.rectusStatLevel), 1);
            editor.putInt(getString(R.string.obliquesStatLevel), 1);
            editor.putInt(getString(R.string.transverseStatLevel), 1);
            editor.putInt(getString(R.string.rectusStatProgress), 0);
            editor.putInt(getString(R.string.obliquesStatProgress), 0);
            editor.putInt(getString(R.string.transverseStatProgress), 0);
            editor.commit();
        }
    }
}
