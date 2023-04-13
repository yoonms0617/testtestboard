package com.backend.token.service;

import com.backend.token.domain.Token;
import com.backend.token.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

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

}
