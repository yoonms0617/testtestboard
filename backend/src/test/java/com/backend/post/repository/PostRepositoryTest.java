package com.backend.post.repository;

import com.backend.global.config.JpaAuditConfig;
import com.backend.member.domain.Member;
import com.backend.member.repository.MemberRepository;
import com.backend.post.domain.Post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditConfig.class)
class PostRepositoryTest {

    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("민수쿤", "yoonms0617", "1q2w3e4r!");
        memberRepository.save(member);
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    void post_save_test() {
        Post post = new Post(TITLE, member.getNickname(), CONTENT, member);

        Post save = postRepository.save(post);

        assertThat(save.getId()).isNotNull();
    }

    @Test
    @DisplayName("게시글 목록을 조회한다.")
    void post_findAll_test() {
        Post postA = new Post(TITLE, member.getNickname(), CONTENT, member);
        Post postB = new Post(TITLE, member.getNickname(), CONTENT, member);
        Post postC = new Post(TITLE, member.getNickname(), CONTENT, member);
        Post postD = new Post(TITLE, member.getNickname(), CONTENT, member);
        Post postE = new Post(TITLE, member.getNickname(), CONTENT, member);
        postRepository.saveAll(List.of(postA, postB, postC, postD, postE));

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<Post> posts = postRepository.findAll(pageable);

        assertThat(posts.getContent()).containsExactly(postE, postD, postC, postB, postA);
        assertThat(posts.getContent().size()).isEqualTo(5);
        assertThat(posts.getTotalElements()).isEqualTo(5);
        assertThat(posts.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("식별자로 게시글을 조회한다.")
    void post_findById_test() {
        Post post = new Post(TITLE, member.getNickname(), CONTENT, member);
        Post save = postRepository.save(post);

        Optional<Post> find = postRepository.findById(save.getId());

        assertThat(find).isPresent();
        assertThat(find.get()).usingRecursiveComparison().isEqualTo(save);
    }

}