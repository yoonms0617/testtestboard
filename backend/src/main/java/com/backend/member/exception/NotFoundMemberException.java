package com.backend.member.exception;

import com.backend.global.error.exception.BaseException;
import com.backend.global.error.exception.ErrorType;

public class NotFoundMemberException extends BaseException {

    public NotFoundMemberException(ErrorType errorType) {
        super(errorType);
    }

}
