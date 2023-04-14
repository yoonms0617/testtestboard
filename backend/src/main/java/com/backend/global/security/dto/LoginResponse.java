package com.backend.global.security.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private final String nickname;

    private final Token token;

    public LoginResponse(String nickname, String accessToken, String refreshToken) {
        this.nickname = nickname;
        this.token = new Token(accessToken, refreshToken);
    }

    @Getter
    private static class Token {

        private final String accessToken;

        private final String refreshToken;

        public Token(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

    }

}
