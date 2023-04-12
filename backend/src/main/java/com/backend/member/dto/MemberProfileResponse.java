package com.backend.member.dto;

import com.backend.member.domain.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberProfileResponse {

    private final String nickname;

    private final String username;

    public MemberProfileResponse(Member member) {
        this.nickname = member.getNickname();
        this.username = member.getUsername();
    }

}
