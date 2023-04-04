package com.backend.member.repository;

import com.backend.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByUsername(String username);

}
