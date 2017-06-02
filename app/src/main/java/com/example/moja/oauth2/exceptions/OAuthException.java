package com.example.moja.oauth2.exceptions;

/**
 * Created by moja on 02.06.2017.
 */

public abstract class OAuthException extends Exception {
    protected OAuthException(String exceptionMsg) {
        super(exceptionMsg);
    }
    protected OAuthException(String exceptionMsg, Exception e) {
        super(exceptionMsg, e);
    }
    public abstract OAuthExceptionType getExceptionType();
}
