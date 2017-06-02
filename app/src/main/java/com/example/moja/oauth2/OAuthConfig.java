package com.example.moja.oauth2;

import java.net.URI;
import java.util.List;

/**
 * Created by moja on 02.06.2017.
 */

public class OAuthConfig {
    private List<String> scopes;
    private URI tokenUri;
    private Integer requestTiemOut;
    private String clientID;
    private String clientSecret;

    public OAuthConfig(List<String> scopes, URI tokenUri, Integer requestTiemOut, String clientID, String clientSecret) {
        this.scopes = scopes;
        this.tokenUri = tokenUri;
        this.requestTiemOut = requestTiemOut;
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

    public Integer getRequestTiemOut() {
        return requestTiemOut;
    }

    public void setRequestTiemOut(Integer requestTiemOut) {
        this.requestTiemOut = requestTiemOut;
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
}
