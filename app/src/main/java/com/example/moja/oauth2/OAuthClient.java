package com.example.moja.oauth2;

import android.util.Base64;

import com.example.moja.oauth2.exceptions.OAuthException;
import com.example.moja.oauth2.exceptions.OAuthExceptionManager;
import com.example.moja.oauth2.exceptions.OAuthExceptionReason;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by moja on 02.06.2017.
 */

public class OAuthClient {
    public interface OAuthCallback {
        void onComplete(OAuthTokenResult tokenResult, Exception e);
    }

    private OAuthConfig oAuthConfig;
    private OkHttpClient okHttpClient = new OkHttpClient();

    private static final String JSON_MEDIA_TYPE = "application/json; charset=utf-8";
    public static final MediaType JSON = MediaType.parse(JSON_MEDIA_TYPE);

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

    private void requestOAuthTokenWithBody(String body, final OAuthCallback callback) {
        String authHeader = createAuthorizationHeader();
        Map<String, String> headerValues = new HashMap<>();
        headerValues.put("Authorization", authHeader);
        headerValues.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        headerValues.put("Accept", "application/json");

        try {
            String rBody = body;
            List<String> scopes = oAuthConfig.getScopes();
            if (!scopes.isEmpty()) {
                String scopeBody = "";
                for (String s : scopes) {
                    scopeBody += s + ":";
                }
                rBody += "&scope=";
                rBody += scopeBody;
            }

            RequestBody requestBody = RequestBody.create(JSON, rBody);
            Headers headers = Headers.of(headerValues);
            Request request = new Request.Builder()
                    .url(oAuthConfig.getTokenUri().toURL())
                    .post(requestBody)
                    .headers(headers)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onComplete(null, new OAuthExceptionManager(e, e.getMessage()
                            , OAuthExceptionReason.REASON_NETWORK_ERROR));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            parseResponseData(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onComplete(null, e);
                        }
                    } else {
                        callback.onComplete(null, parseError(response.code()));
                    }
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OAuthTokenResult parseResponseData(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        String accessToken = jsonObject.getString("access_token");
        String refreshToken = jsonObject.getString("refresh_token");
        int expiresIn = jsonObject.getInt("expires_in");

        return new OAuthTokenResult(accessToken, refreshToken, expiresIn);
    }

    // TODO: CHANGE AFTER ANALYZING
    private OAuthException parseError(int errCode) {
        OAuthException exception = new OAuthExceptionManager("Error: " + errCode, OAuthExceptionReason.REASON_BAD_RESPONSE);
        return exception;
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
