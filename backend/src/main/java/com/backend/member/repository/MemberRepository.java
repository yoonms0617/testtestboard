package com.backend.member.repository;

import com.backend.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByUsername(String username);

}
