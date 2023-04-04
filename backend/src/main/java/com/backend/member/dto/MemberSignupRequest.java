package com.backend.member.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberSignupRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
