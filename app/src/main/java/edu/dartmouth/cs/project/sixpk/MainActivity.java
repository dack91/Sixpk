package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.dartmouth.cs.project.sixpk.database.AbLog;
import edu.dartmouth.cs.project.sixpk.database.Workout;
import edu.dartmouth.cs.project.sixpk.view.SlidingTabLayout;


public class MainActivity extends Activity {

    // Private objects to handle the sliding tab layout
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;

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
        viewPager.setAdapter(myViewPageAdapter);

        // Track which fragment is in focus
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position)
            {
                // Update list view in HistoryFragment
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


        /*
        // TODO    DRIVER CODE IGNORE ME
        // need to make workout difficulty 1-10 instead of -10 to 10

        ArrayList<AbLog> exercises = new ArrayList<>();
        int[] diff = new int[3];

        diff[0] = 3; diff[1] = 4; diff[2] = 5;
        exercises.add(new AbLog("crunches", 1, 1, diff));
        diff[0] = 1; diff[1] = 3; diff[2] = 5;
        exercises.add(new AbLog("plank", 2, 1, diff));
        diff[0] = 7; diff[1] = 6; diff[2] = 5;
        exercises.add(new AbLog("bicycle", 3, 2, diff));
        diff[0] = 9; diff[1] = 7; diff[2] = 5;
        exercises.add(new AbLog("side plank", 4, 2, diff));
        diff[0] = 5; diff[1] = 5; diff[2] = 5;
        exercises.add(new AbLog("supermans", 5, 3, diff));
        diff[0] = 5; diff[1] = 5; diff[2] = 5;
        exercises.add(new AbLog("scissor kicks", 6, 3, diff));

        Workout workout = new Workout(exercises, 8, 0);

        int[] exlist = workout.getExerciseIdList();
        for (int i = 0; i < exlist.length; i++) {
            System.out.println(exlist[i] + " " + workout.getDurationList()[i]);
        }
        */

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
