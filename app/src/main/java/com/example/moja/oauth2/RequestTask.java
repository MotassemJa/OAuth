package com.example.moja.oauth2;

import android.os.AsyncTask;

import com.example.moja.oauth2.credentials.Credentials;
import com.example.moja.oauth2.credentials.CredentialsStore;
import com.example.moja.oauth2.exceptions.OAuthException;
import com.example.moja.oauth2.exceptions.OAuthExceptionManager;
import com.example.moja.oauth2.exceptions.OAuthExceptionReason;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by moja on 07.06.2017.
 */

public class RequestTask extends AsyncTask<Void, Void, Response> {

    private Request mRequest;
    private OAuthClient.OAuthCallback mAuthCallback;
    private CredentialsStore mCredentialsStore;

    public RequestTask(Request request, OAuthClient.OAuthCallback callback, CredentialsStore mCredentialsStore) {
        mRequest = request;
        mAuthCallback = callback;
        this.mCredentialsStore = mCredentialsStore;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected Response doInBackground(Void... params) {
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(mRequest).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (response != null) {
            if (response.isSuccessful()) {
                try {
                    OAuthTokenResult token = parseResponseData(response.body().string());
                    mAuthCallback.onComplete(token, null);
                    return;
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mAuthCallback.onComplete(null, parseError(response.code()));
    }

    private OAuthException parseError(int errCode) {
        return new OAuthExceptionManager("Error: " + errCode, OAuthExceptionReason.REASON_SERVER_ERROR);
    }

    private OAuthTokenResult parseResponseData(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        String accessToken = jsonObject.getString("access_token");
        String refreshToken = jsonObject.getString("refresh_token");
        mCredentialsStore.storeCredentials(new Credentials(refreshToken));
        int expiresIn = jsonObject.getInt("expires_in");

        return new OAuthTokenResult(accessToken, refreshToken, expiresIn);
    }

}
