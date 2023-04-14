package com.backend.member.service;

import com.backend.member.domain.Member;
import com.backend.member.dto.MemberProfileResponse;
import com.backend.member.exception.DuplicateNicknameException;
import com.backend.member.exception.DuplicateUsernameException;
import com.backend.member.exception.NotFoundMemberException;
import com.backend.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.backend.support.fixture.MemberFixture.ENCODED_PASSWORD;
import static com.backend.support.fixture.MemberFixture.MEMBER;
import static com.backend.support.fixture.MemberFixture.NICKNAME;
import static com.backend.support.fixture.MemberFixture.USERNAME;
import static com.backend.support.fixture.MemberFixture.VALID_SIGNUP_REQUEST;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입을 한다.")
    void signup_test() {
        given(memberRepository.existsByNickname(anyString())).willReturn(false);
        given(memberRepository.existsByUsername(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn(ENCODED_PASSWORD);
        given(memberRepository.save(any(Member.class))).willReturn(MEMBER);

        memberService.signup(VALID_SIGNUP_REQUEST);

        then(memberRepository).should(atLeastOnce()).existsByNickname(anyString());
        then(memberRepository).should(atLeastOnce()).existsByUsername(anyString());
        then(passwordEncoder).should(atLeastOnce()).encode(any(String.class));
        then(memberRepository).should(atLeastOnce()).save(any(Member.class));
    }

    @Test
    @DisplayName("닉네임이 중복되면 예외가 발생한다.")
    void signup_duplicate_nickname_test() {
        given(memberRepository.existsByNickname(anyString())).willReturn(true);

        assertThatThrownBy(() -> memberService.signup(VALID_SIGNUP_REQUEST))
                .isInstanceOf(DuplicateNicknameException.class);

        then(memberRepository).should(atLeastOnce()).existsByNickname(anyString());
        then(memberRepository).should(never()).existsByUsername(anyString());
        then(passwordEncoder).should(never()).encode(anyString());
        then(memberRepository).should(never()).save(any(Member.class));
    }

    @Test
    @DisplayName("아이디가 중복되면 예외가 발생한다.")
    void signup_duplicate_username_test() {
        given(memberRepository.existsByNickname(anyString())).willReturn(false);
        given(memberRepository.existsByUsername(anyString())).willReturn(true);

        assertThatThrownBy(() -> memberService.signup(VALID_SIGNUP_REQUEST))
                .isInstanceOf(DuplicateUsernameException.class);

        then(memberRepository).should(atLeastOnce()).existsByNickname(anyString());
        then(memberRepository).should(atLeastOnce()).existsByUsername(anyString());
        then(passwordEncoder).should(never()).encode(anyString());
        then(memberRepository).should(never()).save(any(Member.class));
    }

    @Test
    @DisplayName("아이디로 회원 정보를 조회한다.")
    void profile_test() {
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(MEMBER));

        MemberProfileResponse response = memberService.profile(USERNAME);

        assertThat(response.getUsername()).isEqualTo(USERNAME);
        assertThat(response.getNickname()).isEqualTo(NICKNAME);
        then(memberRepository).should(atLeastOnce()).findByUsername(anyString());
    }

    @Test
    @DisplayName("아이디로 회원 조회시 회원을 찾을 수 없으면 예외가 발생한다.")
    void profile_not_found_member_test() {
        given(memberRepository.findByUsername(anyString())).willThrow(NotFoundMemberException.class);

        assertThatThrownBy(() -> memberService.profile(USERNAME))
                .isInstanceOf(NotFoundMemberException.class);

        then(memberRepository).should(atLeastOnce()).findByUsername(anyString());
    }

}