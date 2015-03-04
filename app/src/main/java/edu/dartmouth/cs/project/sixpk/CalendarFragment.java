package edu.dartmouth.cs.project.sixpk;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import edu.dartmouth.cs.project.sixpk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

  private CalendarView calendar;


  public CalendarFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    View v = inflater.inflate(R.layout.fragment_calendar, container, false);

    initializeCalendar(v);

    return v;
  }

  public void initializeCalendar(View v) {
    calendar = (CalendarView) v.findViewById(R.id.calendarView);

    // sets whether to show the week number.
    calendar.setShowWeekNumber(false);

    // sets the first day of week according to Calendar.
    // here we set Monday as the first day of the Calendar
    calendar.setFirstDayOfWeek(2);

    //The background color for the selected week.
    //calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color));

    //sets the color for the dates of an unfocused month.
    //calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

    //sets the color for the separator line between weeks.
    //calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

    //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
    //calendar.setSelectedDateVerticalBar(R.color.darkgreen);

    //sets the listener to be notified upon selected date change.
    calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
      //show the selected date as a toast
      @Override
      public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
       // Toast.makeText(getActivity().getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
      }
    });
  }


}
