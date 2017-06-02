package com.example.moja.oauth2.exceptions;

/**
 * Created by moja on 02.06.2017.
 */

public class OAuthExceptionManager extends OAuthException {

    private OAuthExceptionReason mOAuthExceptionReason;

    public OAuthExceptionManager(String exceptionMsg, OAuthExceptionReason reason) {
        super(exceptionMsg);
        this.mOAuthExceptionReason = reason;
    }

    public OAuthExceptionManager(Exception e, OAuthExceptionReason reason) {
        super(e.getMessage(), e);
        this.mOAuthExceptionReason = reason;
    }

    public OAuthExceptionManager(Exception e, String msg, OAuthExceptionReason reason) {
        super(msg, e);
        this.mOAuthExceptionReason = reason;
    }

    @Override
    public OAuthExceptionType getExceptionType() {
        OAuthExceptionType type;
        switch (mOAuthExceptionReason) {
            case REASON_INVALID_REQUEST:
                type = OAuthExceptionType.INVALID_REQUEST;
                break;
            case REASON_UNAUTHORIZED_CLIENT:
                type = OAuthExceptionType.UNAUTHORIZED_CLIENT;
                break;
            case REASON_INVALID_CLIENT:
                type = OAuthExceptionType.INVALID_CLIENT;
                break;

            case REASON_INVALID_CALLBACK:
                type = OAuthExceptionType.INVALID_CALLBACK;
                break;

            case REASON_INVALID_GRANT:
                type = OAuthExceptionType.INVALID_GRANT;
                break;
            case REASON_UNSUPPORTED_GRANT_TYPE:
                type = OAuthExceptionType.UNSUPPORTED_GRANT_TYPE;
                break;

            case REASON_INVALID_SCOPE:
                type = OAuthExceptionType.INVALID_SCOPE;
                break;

            case REASON_ACCESS_DENIED:
                type = OAuthExceptionType.ACCESS_DENIED;
                break;

            case REASON_UNSUPPORTED_RESPONSE_TYPE:
                type = OAuthExceptionType.UNSUPPORTED_RESOURCE_TYPE;
                break;

            case REASON_SERVER_ERROR:
                type = OAuthExceptionType.SERVER_ERROR;
                break;

            case REASON_RESOURCE_NOT_FOUND:
                type = OAuthExceptionType.TEMPORARILY_UNAVAILABLE;
                break;

            case REASON_NETWORK_ERROR:
                type = OAuthExceptionType.NETWORK_ERROR;
                break;

            case REASON_INTERNAL_ERROR:
            default:
                type = OAuthExceptionType.INTERNAL_ERROR;
                break;

        }

        return type;
    }
}
