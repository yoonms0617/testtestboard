package com.backend.global.security.handler;

import com.backend.global.error.dto.ErrorResponse;
import com.backend.global.error.exception.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        ErrorType errorType = ErrorType.UNSUPPORTED_ERROR_TYPE;
        if (exception instanceof BadCredentialsException) {
            errorType = ErrorType.BAD_CREDENTIALS;
        }
        response.setStatus(errorType.getStatus());
        response.setCharacterEncoding("utf-8");
        ErrorResponse errorResponse = ErrorResponse.of(errorType);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

}
