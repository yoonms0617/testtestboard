package com.backend.member.domain;

import lombok.Getter;

@Getter
public enum Role {

    MEMBER("ROLE_MEMBER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

}
