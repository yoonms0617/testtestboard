package com.backend.support.fixture;

import com.backend.global.security.dto.LoginMember;
import com.backend.token.domain.Token;
import com.backend.token.dto.TokenResponse;

import static com.backend.support.fixture.JwtFixture.ACCESS_TOKEN;
import static com.backend.support.fixture.JwtFixture.REFRESH_TOKEN;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

public final class AuthFixture {

    private static final Long MEMBER_ID = 1L;
    private static final String NICKNAME = "민수쿤";
    private static final String USERNAME = "yoonms0617";
    private static final String ENCODED_PASSWORD = createDelegatingPasswordEncoder().encode("1q2w3e4r!");
    private static final String ROLE = "ROLE_MEMBER";

    public static LoginMember LOGIN_MEMBER = new LoginMember(MEMBER_ID, NICKNAME, USERNAME, ENCODED_PASSWORD, ROLE);
    public static TokenResponse TOKEN_RESPONSE = new TokenResponse(ACCESS_TOKEN);

    public static Token TOKEN = Token.builder()
            .username(USERNAME)
            .refreshToken(REFRESH_TOKEN)
            .memberId(MEMBER_ID)
            .build();


}
