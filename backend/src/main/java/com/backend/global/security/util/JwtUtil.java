package com.backend.global.security.util;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.exception.ExpiredTokenException;
import com.backend.global.security.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenInMilliseconds;
    private final long refreshTokenInMilliseconds;

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey,
                   @Value("${jwt.access-token.expire-length}") long accessTokenInMilliseconds,
                   @Value("${jwt.refresh-token.expire-length}") long refreshTokenInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenInMilliseconds = accessTokenInMilliseconds;
        this.refreshTokenInMilliseconds = refreshTokenInMilliseconds;
    }

    public String createAccessToken(Long id, String nickname, String username, String role) {
        return createToken(id, nickname, username, role, accessTokenInMilliseconds);
    }

    public String createRefreshToken(Long id, String nickname, String username, String role) {
        return createToken(id, nickname, username, role, refreshTokenInMilliseconds);
    }

    private String createToken(Long id, String nickname, String username, String role, long tokenInMilliseconds) {
        Date iat = new Date();
        Date exp = new Date(iat.getTime() + tokenInMilliseconds);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("nickname", nickname)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getPayload(String token) {
        return getClaims(token).getBody();
    }

    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(ErrorType.EXPIRED_TOKEN.getCode());
        }
    }

    public void validatedToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            validateExpireToken(claims);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN.getCode());
        }
    }

    private void validateExpireToken(Jws<Claims> claims) {
        if (claims.getBody().getExpiration().before(new Date())) {
            throw new ExpiredTokenException(ErrorType.EXPIRED_TOKEN.getCode());
        }
    }

}
