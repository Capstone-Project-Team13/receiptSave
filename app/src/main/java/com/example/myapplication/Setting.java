package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class Setting extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if(findViewById(R.id.fragment) != null) {
            if(savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.fragment, new Notification()).commit();
        }
    }
}