package com.backend.global.security.handler;

import com.backend.global.security.dto.LoginMember;
import com.backend.global.security.dto.LoginResponse;
import com.backend.global.security.util.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        String accessToken = jwtUtil.createAccessToken(loginMember.getId(), loginMember.getUsername(), loginMember.getRole());
        String refreshToken = jwtUtil.createRefreshToken(loginMember.getId(), loginMember.getUsername(), loginMember.getRole());
        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);
        objectMapper.writeValue(response.getOutputStream(), loginResponse);
    }

}
