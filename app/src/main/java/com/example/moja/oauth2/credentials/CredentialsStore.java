package com.example.moja.oauth2.credentials;

/**
 * Created by moja on 06.06.2017.
 */

public interface CredentialsStore {
    void storeCredentials(Credentials credentials);
    Credentials loadCredentials();
}
