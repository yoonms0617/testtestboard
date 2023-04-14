package com.backend.support.fixture;

import com.backend.post.domain.Post;
import com.backend.post.dto.PostDetailResponse;
import com.backend.post.dto.PostWriteRequest;

import java.time.LocalDateTime;

import static com.backend.support.fixture.MemberFixture.MEMBER;
import static com.backend.support.fixture.MemberFixture.NICKNAME;

public class PostFixture {

    private static final String EMPTY_VALUE = "";
    public static final String TITLE = "Hello";
    public static final String CONTENT = "World";

    public static PostWriteRequest VALID_POST_WRITE_REQUEST = new PostWriteRequest(TITLE, CONTENT);
    public static PostWriteRequest INVALID_POST_WRITE_REQUEST = new PostWriteRequest(EMPTY_VALUE, EMPTY_VALUE);
    public static PostDetailResponse POST_DETAIL_RESPONSE = new PostDetailResponse(1L, TITLE, NICKNAME, CONTENT, LocalDateTime.now());

    public static Post POST = Post.builder()
            .title(TITLE)
            .writer(NICKNAME)
            .content(CONTENT)
            .member(MEMBER)
            .build();

}
