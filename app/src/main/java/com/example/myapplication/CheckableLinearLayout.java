package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cd = (CheckBox) findViewById(R.id.checkBox);
        if( cd.isChecked() != checked){
            cd.setChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox cd = (CheckBox) findViewById(R.id.checkBox);

        return cd.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cd = (CheckBox) findViewById(R.id.checkBox);

        setChecked(cd.isChecked() ? false: true);
    }
}
