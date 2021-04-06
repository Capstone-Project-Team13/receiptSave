package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Notification extends PreferenceFragment {
    private SharedPreferences pref;
    private static final String TAG = "Notification";
    public  static final String PREFERENCE_NOTIFICATION = "notification";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                // notification switch
                if(key.equals(PREFERENCE_NOTIFICATION)) {
                    if(sharedPreferences.getBoolean(key,true)){
                        Log.d(TAG,"Preference key is "+key);
                        setNotificaiton();

                    }
                }
                // we can add more setting here

            }
        });
    }

    private void setNotificaiton() {


    }
}
