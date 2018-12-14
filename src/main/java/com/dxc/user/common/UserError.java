package com.dxc.user.common;

import org.springframework.http.HttpStatus;

public enum UserError {
    UNEXPECTED(0, HttpStatus.INTERNAL_SERVER_ERROR),
    USER_INVALID(1, HttpStatus.BAD_REQUEST),
    NOT_FOUND(2000, HttpStatus.NOT_FOUND),
    USER_EXIST_BEFORE(2007, HttpStatus.BAD_REQUEST),
    USER_DOES_NOT_EXIST(2009, HttpStatus.BAD_REQUEST),
    MARK_DELETE(2010,HttpStatus.BAD_REQUEST);

    private final int code;
    private final HttpStatus httpStatus;

    UserError(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
