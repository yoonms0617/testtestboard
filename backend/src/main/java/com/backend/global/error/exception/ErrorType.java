package com.backend.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    UNSUPPORTED_ERROR_TYPE("ERRC001", 400, "지원하지 않는 에러 유형입니다."),
    UNSUPPORTED_METHOD_TYPE("ERRC002", 405, "지원하지 않는 HTTP 메소드를 호출했습니다."),
    INVALID_INPUT_VALUE("ERRC003", 400, "잘못된 입력 값입니다.");

    private final String code;

    private final int status;

    private final String message;

    ErrorType(String code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}
