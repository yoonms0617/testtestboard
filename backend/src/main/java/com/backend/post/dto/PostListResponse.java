package com.backend.post.dto;

import com.backend.post.domain.Post;

import lombok.Getter;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostListResponse {

    private final boolean isFirst;
    private final boolean isLast;
    private final int curPage;
    private final List<PostItem> posts;

    public PostListResponse(List<PostItem> posts, int curPage, boolean isFirst, boolean isLast) {
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.curPage = curPage;
        this.posts = posts;
    }

    public static PostListResponse ofPostPage(Page<Post> postPage) {
        List<PostItem> postitems = postPage.getContent()
                .stream()
                .map(post -> new PostItem(
                        post.getId(),
                        post.getTitle(),
                        post.getWriter(),
                        post.getCreatedAt()
                )).collect(Collectors.toList());
        return new PostListResponse(postitems, postPage.getNumber(), postPage.isFirst(), postPage.isLast());
    }

}
