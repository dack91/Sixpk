package edu.dartmouth.cs.project.sixpk.view;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class MyNumberPicker extends android.widget.NumberPicker {

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    // custom update view to set text size bigger than normal
    private void updateView(View view) {
        if(view instanceof EditText){
            ((EditText) view).setTextSize(24);
            ((EditText) view).setTextColor(Color.parseColor("#ffffff"));
        }
    }

}
