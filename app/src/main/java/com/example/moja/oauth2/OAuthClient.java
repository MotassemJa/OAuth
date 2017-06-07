package com.example.moja.oauth2;

import android.util.Base64;

import com.example.moja.oauth2.credentials.Credentials;
import com.example.moja.oauth2.credentials.CredentialsStore;
import com.example.moja.oauth2.credentials.InMemoryCredentialsStore;
import com.example.moja.oauth2.exceptions.OAuthException;
import com.example.moja.oauth2.exceptions.OAuthExceptionManager;
import com.example.moja.oauth2.exceptions.OAuthExceptionReason;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by moja on 02.06.2017.
 */

public class OAuthClient {
    public interface OAuthCallback {
        void onComplete(OAuthTokenResult tokenResult, Exception e);
    }

    private OAuthConfig oAuthConfig;

    public CredentialsStore getCredentialsStore() {
        return mCredentialsStore;
    }

    private CredentialsStore mCredentialsStore;

    public OAuthClient(OAuthConfig oAuthConfig) {
        this.oAuthConfig = oAuthConfig;
        if (oAuthConfig.getCredentialsStore() != null) {
            mCredentialsStore = oAuthConfig.getCredentialsStore();
        } else {
            mCredentialsStore = new InMemoryCredentialsStore();
        }
    }

    public void requestOAuthTokenWithUsername(String username, String password, OAuthCallback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("grant_type", "password");
        formBuilder.add("username", username);
        formBuilder.add("password", password);
        requestOAuthTokenWithBody(formBuilder, callback);
    }

    public void requestOAuthToken(OAuthCallback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("grant_type", "client_credentials");
        requestOAuthTokenWithBody(formBuilder, callback);
    }

    public void requestOAuthTokenWithRefreshToken(String refreshToken, OAuthCallback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("grant_type", "refresh_token");
        formBuilder.add("refresh_token", refreshToken);
        requestOAuthTokenWithBody(formBuilder, callback);
    }

    private void requestOAuthTokenWithBody(FormBody.Builder body, final OAuthCallback callback) {
        addBodyParams(body);

        try {
            parseScopes(body);

            Request.Builder requestBuilder = new Request.Builder();

            requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .addHeader("Accept", "application/json");

            RequestBody requestBody = body.build();
            Request request = requestBuilder.url(oAuthConfig.getTokenUri().toURL()).post(requestBody).build();

            new RequestTask(request, new OAuthCallback() {
                @Override
                public void onComplete(OAuthTokenResult tokenResult, Exception e) {
                    if (e != null) {
                        callback.onComplete(null, e);
                    } else {
                        callback.onComplete(tokenResult, null);
                    }
                }
            }, mCredentialsStore).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseScopes(FormBody.Builder body) {
        List<String> scopes = oAuthConfig.getScopes();
        if (!scopes.isEmpty()) {
            String scopeBody = "";
            for (String s : scopes) {
                scopeBody += s + ":";
            }
            body.add("scope", scopeBody);
        }
    }

    private void addBodyParams(FormBody.Builder body) {
        String authHeader = createAuthorizationHeader();
        body.add("Authorization", authHeader);
        body.add("client_id", oAuthConfig.getClientID());
        body.add("client_secret", oAuthConfig.getClientSecret());
    }

    private String createAuthorizationHeader() {
        try {
            String authCredentials = URLEncoder.encode(oAuthConfig.getClientID() + ":" + oAuthConfig.getClientSecret(), "UTF-8");
            byte[] data = authCredentials.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
            return "Base " + base64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
