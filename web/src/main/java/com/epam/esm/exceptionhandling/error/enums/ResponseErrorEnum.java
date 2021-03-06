package com.epam.esm.exceptionhandling.error.enums;

import com.epam.esm.constants.WebLayerConstants;
import org.springframework.http.HttpStatus;


public enum ResponseErrorEnum {
    GIFT_CERTIFICATE_NOT_FOUND("gift_certificate_not_found", HttpStatus.NOT_FOUND, WebLayerConstants.CERTIFICATE_NOT_FOUND_CODE),
    GIFT_CERTIFICATE_WITH_SUCH_NAME_EXISTS("gift_certificate_already_exists", HttpStatus.BAD_REQUEST, WebLayerConstants.CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE),
    TAG_NOT_FOUND("tag_not_found", HttpStatus.NOT_FOUND, WebLayerConstants.TAG_NOT_FOUND_ERROR_CODE),
    TAG_WITH_SUCH_NAME_EXISTS("tag_already_exists", HttpStatus.BAD_REQUEST, WebLayerConstants.TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE),
    USER_NOT_FOUND("user_not_found_by_id", HttpStatus.NOT_FOUND, WebLayerConstants.USER_NOT_FOUND_BY_ID_ERROR_CODE),
    USER_WITH_SUCH_LOGIN_EXISTS("user_already_exists", HttpStatus.BAD_REQUEST, WebLayerConstants.USER_SUCH_LOGIN_EXISTS_CODE),
    ORDER_NOT_FOUND("order_not_found", HttpStatus.NOT_FOUND, WebLayerConstants.ORDER_NOT_FOUND_ERROR_CODE),
    MISMATCH_PARAMETER("mismatch_parameter", HttpStatus.BAD_REQUEST, WebLayerConstants.MISMATCH_PARAMETER_ERROR_CODE),
    REFRESH_TOKEN_EXPIRED("refresh_token_expired", HttpStatus.FORBIDDEN, WebLayerConstants.REFRESH_TOKEN_EXPIRED),
    ACCESS_TOKEN_EXPIRED("access_token_expired", HttpStatus.UNAUTHORIZED, WebLayerConstants.ACCESS_TOKEN_EXPIRED),
    TOKEN_INVALID("token_invalid", HttpStatus.UNAUTHORIZED, WebLayerConstants.ACCESS_TOKEN_INVALID),
    ACCESS_DENIED("access_denied", HttpStatus.FORBIDDEN, WebLayerConstants.ACCESS_DENIED_ERROR_CODE),
    UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED, WebLayerConstants.UNAUTHORIZED_ERROR_CODE),
    INVALID_USER_CREDENTIALS("invalid_user_credentials", HttpStatus.BAD_REQUEST, WebLayerConstants.INVALID_CREDENTIALS_ERROR_CODE),
    ACCESS_TOKEN_NOT_FOUND("access_token_not_found", HttpStatus.BAD_REQUEST, WebLayerConstants.ACCESS_TOKEN_NOT_FOUND),
    USER_NOT_FOUND_BY_LOGIN_ERROR_CODE("user_not_found_by_login", HttpStatus.NOT_FOUND, WebLayerConstants.USER_NOT_FOUND_BY_LOGIN_ERROR_CODE);

    private final String propertyKey;
    private final int code;
    private final HttpStatus status;

    ResponseErrorEnum(String propertyKey, HttpStatus status, int code) {
        this.propertyKey = propertyKey;
        this.code = code;
        this.status = status;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
