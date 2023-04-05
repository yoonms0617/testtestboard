package com.backend.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostItem {

    private final Long postNum;
    private final String title;
    private final String writer;
    private final LocalDateTime createdAt;

    @Builder
    public PostItem(Long postNum, String title, String writer, LocalDateTime createdAt) {
        this.postNum = postNum;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
    }

}
