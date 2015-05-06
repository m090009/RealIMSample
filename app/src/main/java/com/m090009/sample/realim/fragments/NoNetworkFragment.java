package com.m090009.sample.realim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tohamy on 5/6/15.
 */
public class NoNetworkFragment extends Fragment{
    private String fName = "NoNetworkFragment";
    public static NoNetworkFragment newInstance(){
        return new NoNetworkFragment();
    }
    public NoNetworkFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}
