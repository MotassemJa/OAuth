package com.example.moja.oauth2.credentials;

/**
 * Created by moja on 06.06.2017.
 */

public class Credentials {
    private String refreshToken;

    public Credentials(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
