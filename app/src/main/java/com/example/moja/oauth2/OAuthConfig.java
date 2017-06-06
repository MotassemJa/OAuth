package com.example.moja.oauth2;

import com.example.moja.oauth2.credentials.CredentialsStore;

import java.net.URI;
import java.util.List;

/**
 * Created by moja on 02.06.2017.
 */

public class OAuthConfig {
    private List<String> scopes;
    private URI tokenUri;
    private Integer timeout;
    private String clientID;
    private String clientSecret;
    private CredentialsStore credentialsStore;

    public OAuthConfig(List<String> scopes, URI tokenUri, Integer timeout, String clientID, String clientSecret) {
        this.scopes = scopes;
        this.tokenUri = tokenUri;
        this.timeout = timeout;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public URI getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(URI tokenUri) {
        this.tokenUri = tokenUri;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public CredentialsStore getCredentialsStore() {
        return credentialsStore;
    }

    public void setCredentialsStore(CredentialsStore credentialsStore) {
        this.credentialsStore = credentialsStore;
    }
}
