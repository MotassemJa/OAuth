package com.example.moja.oauth2;

import com.example.moja.oauth2.credentials.Credentials;
import com.example.moja.oauth2.exceptions.OAuthException;
import com.example.moja.oauth2.exceptions.OAuthExceptionManager;
import com.example.moja.oauth2.exceptions.OAuthExceptionReason;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by moja on 06.06.2017.
 */

public class Authenticator {
    public interface AuthenticationCallback {
        void onAuthenticationCompleted(boolean flag, Exception e);
    }
    public interface AccessTokenCallback {
        void onAccessTokenReceived(String s, Exception e);
    }

    private OAuthConfig mConfig;
    private OAuthClient mClient;
    private OAuthTokenResult mTokenResult;

    public Authenticator(OAuthConfig config) {
        this.mConfig = config;
        mClient = new OAuthClient(mConfig);
    }

    public void authenticateWithUsername(String username, String password, final AuthenticationCallback callback) {
        mClient.requestOAuthTokenWithUsername(username, password, new OAuthClient.OAuthCallback() {
            @Override
            public void onComplete(OAuthTokenResult tokenResult, Exception e) {
                if (e != null) {
                    callback.onAuthenticationCompleted(false, e);
                }
                else {
                    mTokenResult = tokenResult;
                    callback.onAuthenticationCompleted(true, e);
                }
            }
        });
    }

    public void invalidateAuthToken() {

    }

    public void signOut() {
        
    }

    public void retrieveAccessToken(final AccessTokenCallback callback) {
        OAuthTokenResult validAccessToken = null;
        if (isAccessTokenValid()) {
            validAccessToken = mTokenResult;
            if (!validAccessToken.getAccessToken().isEmpty()) {
                callback.onAccessTokenReceived(validAccessToken.getAccessToken(), null);
            }
            else {
                Credentials credentials = mClient.getCredentialsStore().loadCredentials();
                if (credentials != null) {
                    mClient.requestOAuthTokenWithRefreshToken(credentials.getRefreshToken(), new OAuthClient.OAuthCallback() {
                        @Override
                        public void onComplete(OAuthTokenResult tokenResult, Exception e) {
                            if (e != null) {
                                callback.onAccessTokenReceived(null, e);
                            }
                            else {
                                callback.onAccessTokenReceived(tokenResult.getAccessToken(), null);
                            }
                        }
                    });
                }
                else {
                    callback.onAccessTokenReceived(null, new OAuthExceptionManager("No credentials available",
                            OAuthExceptionReason.REASON_UNAUTHORIZED_CLIENT));
                }
            }
        }
    }

    private boolean isAccessTokenValid() {
        if (mTokenResult != null) {
            if (!mTokenResult.getAccessToken().isEmpty()) {
                Calendar c = Calendar.getInstance();
                int currentSeconds = c.get(Calendar.SECOND);
                if (currentSeconds - mTokenResult.getTimestamp() < mTokenResult.getExpiresIn()) {
                    return true;
                }
            }
        }
        return false;
    }
}
