package edu.dartmouth.cs.project.sixpk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Fanglin Chen on 12/18/14.
 * Updated for 6pk 2/16/15
 */

public class ActionTabsViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public static final int START = 0;
    public static final int HISTORY = 1;
    public static final int PROFILE = 2;
    public static final int SCHEDULER = 3;
    public static final String UI_TAB_START = "WORKOUT";
    public static final String UI_TAB_HISTORY = "HISTORY";
    public static final String UI_TAB_PROFILE = "SETTINGS";
    public static final String UI_TAB_CALENDAR = "SCHEDULE";

    public ActionTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int pos){
        return fragments.get(pos);
    }

    public int getCount(){
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case START:
                return UI_TAB_START;
            case HISTORY:
                return UI_TAB_HISTORY;
            case PROFILE:
                return UI_TAB_PROFILE;
            case SCHEDULER:
            return UI_TAB_CALENDAR;
            default:
                break;
        }
        return null;
    }


}
