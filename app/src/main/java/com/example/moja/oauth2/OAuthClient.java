package com.example.moja.oauth2;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by moja on 02.06.2017.
 */

public class OAuthClient {
    public interface OAuthCallback {
        void onComplete(OAuthTokenResult tokenResult, Exception e);
    }

    private OAuthConfig oAuthConfig;

    public OAuthClient(OAuthConfig oAuthConfig) {
        this.oAuthConfig = oAuthConfig;
    }

    public void requestOAuthTokenWithUsername(String username, String password, OAuthCallback callback) {
        String bodyString = "grant_type=password&username=" + username + "&password=" + password;
        requestOAuthTokenWithBody(bodyString, callback);
    }

    public void requestOAuthToken(OAuthCallback callback) {
        String bodyString = "grant_type=client_credentials";
        requestOAuthTokenWithBody(bodyString, callback);
    }

    public void requestOAuthTokenWithRefreshToken(String refreshToken, OAuthCallback callback) {
        String bodyString = "grant_type=refresh_token&refresh_token=" + refreshToken;
        requestOAuthTokenWithBody(bodyString, callback);
    }

    private void requestOAuthTokenWithBody(String body, OAuthCallback callback) {
        String authHeader = createAuthorizationHeader();
    }

    private String createAuthorizationHeader() {
        try {
            String authCredentials = URLEncoder.encode(oAuthConfig.getClientID() + ":" + oAuthConfig.getClientSecret(), "UTF-8");
            byte[] data = authCredentials.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            return "Base " + base64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
