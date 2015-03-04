package edu.dartmouth.cs.project.sixpk;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

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

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles) and ViewPager (different pages of fragment) together.
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
      scheduleNotification(getNotification("10 second delay"), 10000);
    }

  private void scheduleNotification(Notification notification, int delay) {

    Intent notificationIntent = new Intent(this, NotificationPublisher.class);
    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
      | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);

    long futureInMillis = SystemClock.elapsedRealtime() + delay;
    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
    //tg.startTone(ToneGenerator.TONE_PROP_ACK);
  }

  private Notification getNotification(String content) {
    Notification.Builder builder = new Notification.Builder(this);
    builder.setContentTitle("Come Workout Now!");
    builder.setContentText("It will hurt now - but you'll feel good later");
    builder.setSmallIcon(R.drawable.logo1);
    builder.setColor(getResources().getColor(R.color.back_blue));
    return builder.build();
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
