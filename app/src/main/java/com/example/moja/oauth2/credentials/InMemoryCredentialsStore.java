package com.example.moja.oauth2.credentials;

/**
 * Created by moja on 06.06.2017.
 */

public class InMemoryCredentialsStore implements CredentialsStore {

    private Credentials credentials;

    @Override
    public void storeCredentials(Credentials credentials) {

    }

    @Override
    public Credentials loadCredentials() {
        return null;
    }
}
