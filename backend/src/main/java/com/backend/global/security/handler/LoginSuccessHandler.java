package com.backend.global.security.handler;

import com.backend.global.security.dto.LoginMember;
import com.backend.global.security.dto.LoginResponse;
import com.backend.global.security.util.JwtUtil;
import com.backend.token.service.TokenService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        String accessToken = createAccessToken(loginMember);
        String refreshToken = createRefreshToken(loginMember);
        LoginResponse loginResponse = new LoginResponse(loginMember.getNickname(), accessToken, refreshToken);
        syncRefreshToken(loginMember, refreshToken);
        objectMapper.writeValue(response.getOutputStream(), loginResponse);
    }

    private String createAccessToken(LoginMember loginMember) {
        return jwtUtil.createAccessToken(loginMember.getId(), loginMember.getNickname(), loginMember.getUsername(), loginMember.getRole());
    }

    private String createRefreshToken(LoginMember loginMember) {
        return jwtUtil.createRefreshToken(loginMember.getId(), loginMember.getNickname(), loginMember.getUsername(), loginMember.getRole());
    }

    private void syncRefreshToken(LoginMember loginMember, String refreshToken) {
        tokenService.syncRefreshToken(loginMember.getId(), loginMember.getUsername(), refreshToken);
    }

}
