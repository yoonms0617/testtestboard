package com.backend.token.domain;

import com.backend.global.domain.BaseEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TOKEN")
@NoArgsConstructor
@Getter
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String refreshToken;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Builder
    public Token(String username, String refreshToken, Long memberId) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
