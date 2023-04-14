package com.backend.auth.jwt;

import com.backend.global.security.exception.ExpiredTokenException;
import com.backend.global.security.exception.InvalidTokenException;
import com.backend.global.security.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class JwtUtilTest {

    private static final Long ID = 1L;
    private static final String NICKNAME = "민수쿤";
    private static final String USERNAME = "yoonms0617";
    private static final String ROLE = "ROLE_MEMBER";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("Access Token을 생성한다.")
    void create_access_token_test() {
        String accessToken = jwtUtil.createAccessToken(ID, NICKNAME, USERNAME, ROLE);

        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("Refresh Token을 생성한다.")
    void create_refresh_token_test() {
        String refreshToken = jwtUtil.createRefreshToken(ID, NICKNAME, USERNAME, ROLE);

        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("올바른 Access Token 정보로 Payload를 조회한다.")
    void payload_from_accessToken() {
        String accessToken = jwtUtil.createAccessToken(ID, NICKNAME, USERNAME, ROLE);

        Claims payload = jwtUtil.getPayload(accessToken);

        assertThat(payload).isNotNull();
        assertThat(Long.valueOf(payload.getSubject())).isEqualTo(ID);
        assertThat(String.valueOf(payload.get("username"))).isEqualTo(USERNAME);
        assertThat(String.valueOf(payload.get("role"))).isEqualTo(ROLE);
    }

    @Test
    @DisplayName("올바른 Refresh Token 정보로 Payload를 조회한다.")
    void payload_from_refreshToken() {
        String refreshToken = jwtUtil.createRefreshToken(ID, NICKNAME, USERNAME, ROLE);

        Claims payload = jwtUtil.getPayload(refreshToken);

        assertThat(payload).isNotNull();
        assertThat(Long.valueOf(payload.getSubject())).isEqualTo(ID);
        assertThat(String.valueOf(payload.get("username"))).isEqualTo(USERNAME);
        assertThat(String.valueOf(payload.get("role"))).isEqualTo(ROLE);
    }

    @Test
    @DisplayName("토큰이 유효하지 않는 경우 예외가 발생한다.")
    void invalid_token_test() {
        String invalidToken = "";

        assertThatThrownBy(() -> jwtUtil.validatedToken(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰의 유효 기간이 만료되었을 경우 예외가 발생한다")
    void expired_token_test() {
        String expiredToken = Jwts.builder()
                .setSubject(String.valueOf(ID))
                .claim("username", USERNAME)
                .claim("role", ROLE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() - 1))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        assertThatThrownBy(() -> jwtUtil.validatedToken(expiredToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

}
