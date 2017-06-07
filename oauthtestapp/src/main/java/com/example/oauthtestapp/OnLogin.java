package com.example.oauthtestapp;

/**
 * Created by moja on 07.06.2017.
 */

public interface OnLogin {
    void onLoginSuccess();

    void onLoginFailed(Exception e);
}