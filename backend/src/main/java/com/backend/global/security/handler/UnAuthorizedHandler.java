package com.backend.global.security.handler;

import com.backend.global.error.dto.ErrorResponse;
import com.backend.global.error.exception.ErrorType;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class UnAuthorizedHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String code = authException.getMessage();
        ErrorType errorType = ErrorType.findByCode(code);
        ErrorResponse errorResponse = ErrorResponse.of(errorType);
        response.setCharacterEncoding("utf-8");
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }

}
