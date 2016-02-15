package com.byteshaft.streamsound.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byteshaft.streamsound.R;

public class SocialMediaFragment extends Fragment {


    public static SocialMediaFragment getFragment() {
        SocialMediaFragment fragment = new SocialMediaFragment();
        return fragment;
    }

    private View mBaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.social_media_fragment, container, false);
        System.out.println("Social");
        return mBaseView;
    }

}
