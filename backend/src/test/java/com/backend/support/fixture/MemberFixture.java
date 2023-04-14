package com.backend.support.fixture;

import com.backend.member.domain.Member;
import com.backend.member.dto.MemberProfileResponse;
import com.backend.member.dto.MemberSignupRequest;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

public final class MemberFixture {

    private static final String EMPTY_VALUE = "";

    public static final Long MEMBER_ID = 1L;
    public static final String NICKNAME = "민수쿤";
    public static final String USERNAME = "yoonms0617";
    public static final String PLAIN_PASSWORD = "1q2w3e4r!";
    public static final String ENCODED_PASSWORD = createDelegatingPasswordEncoder().encode(PLAIN_PASSWORD);

    public static final MemberSignupRequest VALID_SIGNUP_REQUEST = new MemberSignupRequest(NICKNAME, USERNAME, PLAIN_PASSWORD);
    public static final MemberSignupRequest INVALID_SIGNUP_REQUEST = new MemberSignupRequest(EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE);
    public static final MemberProfileResponse MEMBER_PROFILE_RESPONSE = new MemberProfileResponse(NICKNAME, USERNAME);

    public static final Member MEMBER = Member.builder()
            .nickname(NICKNAME)
            .username(USERNAME)
            .password(ENCODED_PASSWORD)
            .build();

}
