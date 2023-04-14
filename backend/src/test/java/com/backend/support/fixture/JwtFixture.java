package com.backend.support.fixture;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

import java.util.Date;

public final class JwtFixture {

    private static final String EMPTY_VALUE = "";
    private static final Long ID = 1L;
    private static final String NICKNAME = "민수쿤";
    private static final String USERNAME = "yoonms0617";
    private static final String ROLE = "ROLE_MEMBER";
    private static final String SECRET_KEY = "this-is-jwt-fixture-test-secret-key";
    private static final long ACCESS_TOKEN_IN_MILLISECONDS = 600000;
    private static final long REFRESH_TOKEN_IN_MILLISECONDS = 3600000;

    public static String ACCESS_TOKEN = createToken(ACCESS_TOKEN_IN_MILLISECONDS);
    public static String REFRESH_TOKEN = createToken(REFRESH_TOKEN_IN_MILLISECONDS);
    public static String INVALID_ACCESS_TOKEN = EMPTY_VALUE;
    public static String INVALID_REFRESH_TOKEN = EMPTY_VALUE;
    public static Claims CLAIMS_FOR_ACCESS_TOKEN = claims(ACCESS_TOKEN);
    public static Claims CLAIMS_FOR_REFRESH_TOKEN = claims(REFRESH_TOKEN);

    private static String createToken(long tokenInMilliseconds) {
        Date iat = new Date();
        Date exp = new Date(iat.getTime() + tokenInMilliseconds);
        return Jwts.builder()
                .setSubject(String.valueOf(ID))
                .claim("nickname", NICKNAME)
                .claim("username", USERNAME)
                .claim("role", ROLE)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Claims claims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
