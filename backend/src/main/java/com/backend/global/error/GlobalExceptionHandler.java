package com.backend.global.error;

import com.backend.global.error.dto.ErrorResponse;
import com.backend.global.error.exception.BaseException;
import com.backend.global.error.exception.ErrorType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException() {
        ErrorResponse response = ErrorResponse.of(ErrorType.INVALID_INPUT_VALUE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handle() {
        ErrorResponse response = ErrorResponse.of(ErrorType.UNSUPPORTED_METHOD_TYPE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
