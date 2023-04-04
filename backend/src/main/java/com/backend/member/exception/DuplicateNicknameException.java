package com.backend.member.exception;

import com.backend.global.error.exception.BaseException;
import com.backend.global.error.exception.ErrorType;

public class DuplicateNicknameException extends BaseException {

    public DuplicateNicknameException(ErrorType errorType) {
        super(errorType);
    }

}
