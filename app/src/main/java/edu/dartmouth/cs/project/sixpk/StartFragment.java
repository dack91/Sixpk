package edu.dartmouth.cs.project.sixpk;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {
    private Button startWorkout;
    private NumberPicker mNumPicker;
    private RadioGroup mDifficulty;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view in the start xml
        final View view = inflater.inflate(R.layout.fragment_start, container, false);

        // Set range for time
        mNumPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        mNumPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // disable keyboard
        mNumPicker.setMinValue(1);
        mNumPicker.setMaxValue(30);

        // Set reference to difficulty radio group
        mDifficulty = (RadioGroup) view.findViewById(R.id.difficulty_radioGroup);

        // Start workout preview
        startWorkout = (Button) view.findViewById(R.id.buttonStartWorkoutPrev);
        startWorkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PreviewActivity.class);
                int duration = mNumPicker.getValue();
                int difficulty = mDifficulty.indexOfChild(view.findViewById(
                        mDifficulty.getCheckedRadioButtonId()));

                i.putExtra(Globals.WORKOUT_DURATION_KEY, duration);
                i.putExtra(Globals.WORKOUT_DIFFICULTY_KEY, difficulty);

                Log.d("DEBUG", "duration/difficulty: " + duration + "/" + difficulty);

                startActivity(i);
            }
            });

        return view;
    }
}