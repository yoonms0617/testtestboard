package com.backend.member.repository;

import com.backend.global.config.JpaAuditConfig;
import com.backend.member.domain.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static com.backend.support.fixture.MemberFixture.ENCODED_PASSWORD;
import static com.backend.support.fixture.MemberFixture.NICKNAME;
import static com.backend.support.fixture.MemberFixture.USERNAME;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 저장한다.")
    void member_save_test() {
        Member member = new Member(NICKNAME, USERNAME, ENCODED_PASSWORD);

        Long id = memberRepository.save(member).getId();

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("식별자로 회원을 조회한다.")
    void member_findById_test() {
        Member member = new Member(NICKNAME, USERNAME, ENCODED_PASSWORD);
        Member expected = memberRepository.save(member);

        Optional<Member> actual = memberRepository.findById(expected.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("아이디로 회원을 조회한다.")
    void member_findbyUsername_test() {
        Member member = new Member(NICKNAME, USERNAME, ENCODED_PASSWORD);
        Member expected = memberRepository.save(member);

        Optional<Member> actual = memberRepository.findByUsername(expected.getUsername());

        assertThat(actual).isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("닉네임의 존재 여부를 확인한다.")
    void member_existsByNickname_test() {
        Member member = new Member(NICKNAME, USERNAME, ENCODED_PASSWORD);
        memberRepository.save(member);

        boolean exists = memberRepository.existsByNickname(NICKNAME);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("아이디의 존재 여부를 확인한다.")
    void member_existsByUsername_test() {
        Member member = new Member(NICKNAME, USERNAME, ENCODED_PASSWORD);
        memberRepository.save(member);

        boolean exists = memberRepository.existsByUsername(USERNAME);

        assertThat(exists).isTrue();
    }

}