package com.backend.member.service;

import com.backend.global.error.exception.ErrorType;
import com.backend.member.domain.Member;
import com.backend.member.dto.MemberSignupRequest;
import com.backend.member.exception.DuplicateNicknameException;
import com.backend.member.exception.DuplicateUsernameException;
import com.backend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(MemberSignupRequest request) {
        existsNickname(request.getNickname());
        existsUsername(request.getUsername());
        String encoded = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .nickname(request.getNickname())
                .username(request.getUsername())
                .password(encoded)
                .build();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void existsNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException(ErrorType.DUPLICATE_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public void existsUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException(ErrorType.DUPLICATE_USERNAME);
        }
    }

}
