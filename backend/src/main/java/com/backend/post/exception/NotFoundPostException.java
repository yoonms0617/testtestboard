package com.backend.post.exception;

import com.backend.global.error.exception.BaseException;
import com.backend.global.error.exception.ErrorType;

public class NotFoundPostException extends BaseException {

    public NotFoundPostException(ErrorType errorType) {
        super(errorType);
    }

}
