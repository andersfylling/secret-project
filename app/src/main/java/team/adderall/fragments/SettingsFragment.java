package team.adderall.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.adderall.R;

public class SettingsFragment
        extends Fragment
{
    @Override
    /**
     * Add settings for setting local language
     * And save it to shared pref.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_view, container, false);
    }
}
