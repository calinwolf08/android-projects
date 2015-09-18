package com.example.calin.navdrawer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by calin on 8/2/15.
 */
public class AboutMeFragment extends Fragment {

    public static AboutMeFragment newInstance() {

        AboutMeFragment fragment = new AboutMeFragment();
        return fragment;

    }

    public AboutMeFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        return rootView;

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(1);

    }
}
