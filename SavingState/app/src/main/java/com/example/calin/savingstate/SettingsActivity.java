package com.example.calin.savingstate;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by calin on 7/26/15.
 */
public class SettingsActivity extends PreferenceActivity{
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
