package com.backend.token.service;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.exception.InvalidTokenException;
import com.backend.global.security.util.JwtUtil;
import com.backend.token.domain.Token;
import com.backend.token.dto.TokenResponse;
import com.backend.token.repository.TokenRepository;

import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void syncRefreshToken(Long memberId, String username, String refreshToken) {
        tokenRepository.findByUsername(username).ifPresentOrElse(
                token -> token.updateRefreshToken(refreshToken),
                () -> tokenRepository.save(Token.builder()
                        .username(username)
                        .refreshToken(refreshToken)
                        .memberId(memberId)
                        .build())
        );
    }

    @Transactional(readOnly = true)
    public TokenResponse reIssueAccessToken(String refreshToken) {
        jwtUtil.validatedToken(refreshToken);
        Claims payload = jwtUtil.getPayload(refreshToken);
        String username = String.valueOf(payload.get("username"));
        Token token = tokenRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidTokenException(ErrorType.INVALID_TOKEN.getCode()));
        boolean isSameRefreshToken = isSameRefreshToken(token.getRefreshToken(), refreshToken);
        if (!isSameRefreshToken) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN.getCode());
        }
        String newAccessToken = createNewAccessToken(payload);
        return new TokenResponse(newAccessToken);
    }

    private boolean isSameRefreshToken(String dbRefreshToken, String reqRefreshToken) {
        return dbRefreshToken.equals(reqRefreshToken);
    }

    private String createNewAccessToken(Claims payload) {
        Long memberId = Long.valueOf(payload.getSubject());
        String username = String.valueOf(payload.get("username"));
        String role = String.valueOf(payload.get("role"));
        return jwtUtil.createAccessToken(memberId, username, role);
    }

}
