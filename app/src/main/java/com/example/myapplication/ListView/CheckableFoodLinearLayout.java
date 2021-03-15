package com.example.myapplication.ListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class CheckableFoodLinearLayout extends LinearLayout implements Checkable {

    public CheckableFoodLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cd = (CheckBox) findViewById(R.id.foodCheckBox);
        if( cd.isChecked() != checked){
            cd.setChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        CheckBox cd = (CheckBox) findViewById(R.id.foodCheckBox);

        return cd.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cd = (CheckBox) findViewById(R.id.foodCheckBox);

        setChecked(cd.isChecked() ? false: true);
    }
}
