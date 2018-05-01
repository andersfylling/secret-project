package team.adderall.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;


import team.adderall.FragmentListner;
import team.adderall.R;

public class SettingsFragment
        extends Fragment
{


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * We have to start the settings fragment from here as
         * PreferenceFragment can not be cast to Fragment.
         */
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new Settings())
               .commit();

    }

    public static class Settings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        public Settings() {
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
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            FragmentListner fl = (FragmentListner) this.getActivity();
            fl.updateLanguage();

        }

    }


}
