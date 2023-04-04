package com.backend.member.exception;

import com.backend.global.error.exception.BaseException;
import com.backend.global.error.exception.ErrorType;

public class DuplicateUsernameException extends BaseException {

    public DuplicateUsernameException(ErrorType errorType) {
        super(errorType);
    }

}
