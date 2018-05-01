package team.adderall.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;


import team.adderall.FragmentListner;
import team.adderall.R;

public class SettingsFragment
        extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    /**
     * on shared preference changed
     * Tell mainactivity to set the new language as the current language.
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        FragmentListner fl = (FragmentListner) this.getActivity();
        fl.updateLanguage();
    }

}
