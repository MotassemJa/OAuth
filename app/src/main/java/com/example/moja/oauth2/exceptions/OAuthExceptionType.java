package com.example.moja.oauth2.exceptions;

/**
 * Created by moja on 02.06.2017.
 */

public enum OAuthExceptionType {
    INTERNAL_ERROR,
    INVALID_REQUEST,
    INVALID_CALLBACK,
    INVALID_GRANT,
    UNAUTHORIZED_CLIENT,
    INVALID_CLIENT,
    RESOURCE_NOT_FOUND,
    UNSUPPORTED_GRANT_TYPE,
    INVALID_SCOPE,
    ACCESS_DENIED,
    UNSUPPORTED_RESOURCE_TYPE,
    SERVER_ERROR,
    INVALID_RESPONSE,
    TEMPORARILY_UNAVAILABLE,
    NETWORK_ERROR,
    OTHER
}
