package com.example.a3aetim.prototipomobilebdinterno.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.a3aetim.prototipomobilebdinterno.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.fragment_settings, s);
    }
}
