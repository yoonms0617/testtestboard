package com.backend.post.dto;

import com.backend.post.domain.Post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PostDetailResponse {

    private Long postNum;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

    public PostDetailResponse(Post post) {
        this.postNum = post.getId();
        this.title = post.getTitle();
        this.writer = post.getWriter();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
    }

}
