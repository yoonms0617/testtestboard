package com.backend.member.repository;

import com.backend.global.config.JpaAuditConfig;
import com.backend.member.domain.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditConfig.class)
class MemberRepositoryTest {

    private static final String NICKNAME = "민수쿤";
    private static final String USERNAME = "yoonms0617";
    private static final String PASSWORD = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("1q2w3e4r!");

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 저장한다.")
    void member_save_test() {
        Member member = new Member(NICKNAME, USERNAME, PASSWORD);

        Long id = memberRepository.save(member).getId();

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("식별자로 회원을 조회한다.")
    void member_findById_test() {
        Member member = new Member(NICKNAME, USERNAME, PASSWORD);
        Member save = memberRepository.save(member);

        Optional<Member> find = memberRepository.findById(save.getId());

        assertThat(find).isPresent();
        assertThat(find.get()).usingRecursiveComparison().isEqualTo(save);
    }

    @Test
    @DisplayName("아이디로 회원을 조회한다.")
    void member_findbyUsername_test() {
        Member member = new Member(NICKNAME, USERNAME, PASSWORD);
        Member save = memberRepository.save(member);

        Optional<Member> find = memberRepository.findByUsername(save.getUsername());

        assertThat(find).isPresent();
        assertThat(find.get()).usingRecursiveComparison().isEqualTo(save);
    }

    @Test
    @DisplayName("닉네임의 존재 여부를 확인한다.")
    void member_existsByNickname_test() {
        Member member = new Member(NICKNAME, USERNAME, PASSWORD);
        memberRepository.save(member);

        boolean exists = memberRepository.existsByNickname(member.getNickname());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("아이디의 존재 여부를 확인한다.")
    void member_existsByUsername_test() {
        Member member = new Member(NICKNAME, USERNAME, PASSWORD);
        memberRepository.save(member);

        boolean exists = memberRepository.existsByUsername(member.getUsername());

        assertThat(exists).isTrue();
    }

}