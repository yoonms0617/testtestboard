package com.backend.global.error.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ErrorType {

    UNSUPPORTED_ERROR_TYPE("ERRC001", 400, "지원하지 않는 에러 유형입니다."),
    UNSUPPORTED_METHOD_TYPE("ERRC002", 405, "지원하지 않는 HTTP 메소드를 호출했습니다."),
    INVALID_INPUT_VALUE("ERRC003", 400, "잘못된 입력 값입니다."),

    BAD_CREDENTIALS("ERRA001", 400, "아이디 또는 비밀번호를 잘못 입력했습니다."),
    INVALID_TOKEN("ERRA002", 401, "토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN("ERRA003", 401, "토큰이 만료되었습니다."),
    ACCESS_DENIED("ERRA004", 403, "권한이 없습니다."),

    DUPLICATE_NICKNAME("ERRM001", 409, "사용 중인 닉네임입니다."),
    DUPLICATE_USERNAME("ERRM002", 409, "사용 중인 아이디입니다."),
    NOT_FOUND_MEMBER("ERRM003", 404, "회원을 찾을 수 없습니다."),

    NOT_FOUND_POST("ERRP001", 404, "게시글을 찾을 수 없습니다.");

    private final String code;
    private final int status;
    private final String message;

    ErrorType(String code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public static ErrorType findByCode(String code) {
        return Arrays.stream(ErrorType.values())
                .filter(errorType -> errorType.getCode().equals(code))
                .findFirst()
                .orElse(UNSUPPORTED_ERROR_TYPE);
    }

}
