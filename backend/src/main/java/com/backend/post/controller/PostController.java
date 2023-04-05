package com.backend.post.controller;

import com.backend.global.security.dto.LoginMember;
import com.backend.post.dto.PostDetailResponse;
import com.backend.post.dto.PostListResponse;
import com.backend.post.dto.PostUpdateRequest;
import com.backend.post.dto.PostWriteRequest;
import com.backend.post.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/write")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<Void> write(@AuthenticationPrincipal LoginMember loginMember,
                                      @Valid @RequestBody PostWriteRequest request) {
        postService.write(loginMember.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<PostListResponse> list(@RequestParam(value = "page", defaultValue = "0") int page) {
        page = (page <= 0) ? 0 : page - 1;
        PostListResponse response = postService.list(page);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/detail/{postNum}")
    public ResponseEntity<PostDetailResponse> detail(@PathVariable("postNum") Long postNum) {
        PostDetailResponse response = postService.detail(postNum);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update/{postNum}")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<Void> update(@PathVariable("postNum") Long postNum,
                                       @Valid @RequestBody PostUpdateRequest request) {
        postService.update(postNum, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{postNum}")
    public ResponseEntity<Void> delete(@PathVariable("postNum") Long postNum) {
        postService.delete(postNum);
        return ResponseEntity.ok().build();
    }

}
