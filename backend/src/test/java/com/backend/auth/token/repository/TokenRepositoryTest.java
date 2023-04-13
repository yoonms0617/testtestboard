package com.backend.auth.token.repository;

import com.backend.global.config.JpaAuditConfig;
import com.backend.token.domain.Token;
import com.backend.token.repository.TokenRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditConfig.class)
class TokenRepositoryTest {

    private static final Long MEMBER_ID = 1L;
    private static final String USERNAME = "yoonms0617";
    private static final String REFRESH_TOKEN = "refreshToken";

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @DisplayName("토큰을 저장한다.")
    void token_save_test() {
        Token token = new Token(USERNAME, REFRESH_TOKEN, MEMBER_ID);

        Long id = tokenRepository.save(token).getId();

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("회원 아이디로 토큰을 조회한다.")
    void token_username_test() {
        Token token = new Token(USERNAME, REFRESH_TOKEN, MEMBER_ID);
        Token save = tokenRepository.save(token);

        Optional<Token> find = tokenRepository.findByUsername(USERNAME);

        assertThat(find).isPresent();
        assertThat(find.get()).usingRecursiveComparison().isEqualTo(save);
    }

}