package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.InitialAbInputs;
import edu.dartmouth.cs.project.sixpk.database.Workout;
import edu.dartmouth.cs.project.sixpk.database.WorkoutEntryDataSource;
import edu.dartmouth.cs.project.sixpk.view.SlidingTabLayout;


public class MainActivity extends Activity {

    // Private objects to handle the sliding tab layout
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;

    private SharedPreferences prefs;

    // Private database to store workout information
//    private WorkoutDataSource datasource;

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
        fragments.add(new HistoryFragment());
        fragments.add(new ProfileFragment());

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

        // Track which fragment is in focus
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position)
            {
                // TODO Update list view in HistoryFragment
                // Call function in HistoryFragment to refresh the UI
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

        // make sure the tabs are equally spaced.
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);


        String mKey = getString(R.string.prefs_key);
        prefs = getSharedPreferences(mKey, MODE_PRIVATE);
        Log.d("INITAL ABLOG INPUT", prefs.contains(getString(R.string.initial_input)) + "");

        if (prefs.getBoolean(getString(R.string.initial_input), true)) {

            Log.d("INITAL ABLOG INPUT", "happening @now");

            WorkoutEntryDataSource dbHelper = new WorkoutEntryDataSource(this);
            dbHelper.open();

            int i = 0;
            for (int j = 0; j < InitialAbInputs.rectusArray.length; j++) {
                dbHelper.insertAblogEntry(new AbLog(InitialAbInputs.rectusArray[j],
                        i, InitialAbInputs.RECTUS, InitialAbInputs.rectusGIFArray[j]));
                i++;
            }
            for (int j = 0; j < InitialAbInputs.obliqueArray.length; j++) {
                dbHelper.insertAblogEntry(new AbLog(InitialAbInputs.obliqueArray[j],
                        i, InitialAbInputs.OBLIQUES, InitialAbInputs.obliqueGIFArray[j]));
                i++;
            }
            for (int j = 0; j < InitialAbInputs.tranverseArray.length; j++) {
                dbHelper.insertAblogEntry(new AbLog(InitialAbInputs.tranverseArray[j],
                        i, InitialAbInputs.TRANSVERSE, InitialAbInputs.transverseGIFArray[j]));
                i++;
            }

            dbHelper.close();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(getString(R.string.initial_input), false);
            editor.commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            return rootView;
        }
    }

    // Load preferences XML resource when PreferenceFragment is active
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource for settingsFragment
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
