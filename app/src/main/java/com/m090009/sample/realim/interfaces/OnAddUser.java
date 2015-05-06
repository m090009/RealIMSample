package com.m090009.sample.realim.interfaces;

/**
 * Created by tohamy on 5/4/15.
 */
public interface OnAddUser {
    public void onFinishAddUserDialog(String userName, int position);
    public boolean hasUserName(String username, int position);
}
