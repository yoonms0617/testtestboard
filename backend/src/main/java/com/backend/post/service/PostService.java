package com.backend.post.service;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.exception.AccessDeniedException;
import com.backend.member.domain.Member;
import com.backend.member.exception.NotFoundMemberException;
import com.backend.member.repository.MemberRepository;
import com.backend.post.domain.Post;
import com.backend.post.dto.PostDetailResponse;
import com.backend.post.dto.PostListResponse;
import com.backend.post.dto.PostUpdateRequest;
import com.backend.post.dto.PostWriteRequest;
import com.backend.post.exception.NotFoundPostException;
import com.backend.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void write(String username, PostWriteRequest request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));
        Post post = Post.builder()
                .title(request.getTitle())
                .writer(member.getNickname())
                .content(request.getContent())
                .member(member)
                .build();
        postRepository.save(post);
    }

    @Transactional
    public PostListResponse list(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");
        Page<Post> postPage = postRepository.findAll(pageable);
        return PostListResponse.ofPostPage(postPage);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse detail(Long postNum) {
        Post post = postRepository.findById(postNum)
                .orElseThrow(() -> new NotFoundPostException(ErrorType.NOT_FOUND_POST));
        return new PostDetailResponse(post);
    }

    @Transactional
    public void update(Long postNum, String nickname, PostUpdateRequest request) {
        Post post = postRepository.findById(postNum)
                .orElseThrow(() -> new NotFoundPostException(ErrorType.NOT_FOUND_POST));
        checkPostOwner(nickname, post.getWriter());
        post.update(request.getTitle(), request.getContent());
    }

    @Transactional
    public void delete(Long postNum, String nickname) {
        Post post = postRepository.findById(postNum)
                .orElseThrow(() -> new NotFoundPostException(ErrorType.NOT_FOUND_POST));
        checkPostOwner(nickname, post.getWriter());
        postRepository.delete(post);
    }

    private void checkPostOwner(String nickname, String writer) {
        if (!nickname.equals(writer)) {
            throw new AccessDeniedException(ErrorType.ACCESS_DENIED.getCode());
        }
    }

}
