package com.backend.post.service;

import com.backend.global.error.exception.ErrorType;
import com.backend.member.exception.NotFoundMemberException;
import com.backend.member.repository.MemberRepository;
import com.backend.post.domain.Post;
import com.backend.post.dto.PostDetailResponse;
import com.backend.post.dto.PostListResponse;
import com.backend.post.exception.NotFoundPostException;
import com.backend.post.repository.PostRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.backend.support.fixture.MemberFixture.MEMBER;
import static com.backend.support.fixture.MemberFixture.NICKNAME;
import static com.backend.support.fixture.MemberFixture.USERNAME;
import static com.backend.support.fixture.PostFixture.CONTENT;
import static com.backend.support.fixture.PostFixture.POST;
import static com.backend.support.fixture.PostFixture.TITLE;
import static com.backend.support.fixture.PostFixture.VALID_POST_WRITE_REQUEST;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글을 작성한다.")
    void write_test() {
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(MEMBER));
        given(postRepository.save(any(Post.class))).willReturn(POST);

        postService.write(USERNAME, VALID_POST_WRITE_REQUEST);

        then(memberRepository).should(atLeastOnce()).findByUsername(anyString());
        then(postRepository).should(atLeastOnce()).save(any(Post.class));
    }

    @Test
    @DisplayName("사용자을 찾을 수 없을시 예외가 발생한다.")
    void write_not_found_member_test() {
        given(memberRepository.findByUsername(anyString())).willThrow(new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));

        assertThatThrownBy(() -> postService.write(USERNAME, VALID_POST_WRITE_REQUEST))
                .isInstanceOf(NotFoundMemberException.class);

        then(memberRepository).should(atLeastOnce()).findByUsername(anyString());
        then(postRepository).should(never()).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 목록을 조회한다.")
    void list_test() {
        List<Post> posts = Arrays.asList(
                new Post(TITLE, NICKNAME, CONTENT, MEMBER),
                new Post(TITLE, NICKNAME, CONTENT, MEMBER),
                new Post(TITLE, NICKNAME, CONTENT, MEMBER),
                new Post(TITLE, NICKNAME, CONTENT, MEMBER),
                new Post(TITLE, NICKNAME, CONTENT, MEMBER)
        );
        Pageable pageable = PageRequest.of(1, 5, Sort.Direction.DESC, "id");
        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());

        given(postRepository.findAll(any(Pageable.class))).willReturn(postPage);

        PostListResponse response = postService.list(1);

        assertThat(response.getPosts().size()).isEqualTo(5);
        then(postRepository).should(atLeastOnce()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("게시글을 상세 조회한다.")
    void detail_test() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));

        PostDetailResponse response = postService.detail(1L);

        assertThat(response.getTitle()).isEqualTo(TITLE);
        assertThat(response.getWriter()).isEqualTo(NICKNAME);
        assertThat(response.getContent()).isEqualTo(CONTENT);
        then(postRepository).should(atLeastOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("게시글을 찾을 수 없을시 예외가 발생한다.")
    void detail_not_found_post_test() {
        given(postRepository.findById(anyLong())).willThrow(new NotFoundPostException(ErrorType.NOT_FOUND_POST));

        assertThatThrownBy(() -> postService.detail(1L))
                .isInstanceOf(NotFoundPostException.class);

        then(postRepository).should(atLeastOnce()).findById(anyLong());
    }

}