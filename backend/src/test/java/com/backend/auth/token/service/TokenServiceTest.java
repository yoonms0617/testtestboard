package com.backend.auth.token.service;

import com.backend.global.security.util.JwtUtil;
import com.backend.token.domain.Token;
import com.backend.token.repository.TokenRepository;
import com.backend.token.service.TokenService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.backend.support.fixture.AuthFixture.TOKEN;
import static com.backend.support.fixture.JwtFixture.REFRESH_TOKEN;
import static com.backend.support.fixture.MemberFixture.MEMBER_ID;
import static com.backend.support.fixture.MemberFixture.USERNAME;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("새로운 토큰을 저장한다.")
    void syncRefreshToken_test() {
        given(tokenRepository.save(any(Token.class))).willReturn(TOKEN);

        tokenService.syncRefreshToken(MEMBER_ID, USERNAME, REFRESH_TOKEN);

        then(tokenRepository).should(atLeastOnce()).save(any(Token.class));
    }

}