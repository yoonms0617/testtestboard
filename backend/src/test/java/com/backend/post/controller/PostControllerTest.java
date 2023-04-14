package com.backend.post.controller;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.config.SecurityConfig;
import com.backend.global.security.util.JwtUtil;
import com.backend.post.dto.PostItem;
import com.backend.post.dto.PostListResponse;
import com.backend.post.dto.PostWriteRequest;
import com.backend.post.exception.NotFoundPostException;
import com.backend.post.service.PostService;
import com.backend.token.service.TokenService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.backend.support.fixture.JwtFixture.ACCESS_TOKEN;
import static com.backend.support.fixture.JwtFixture.CLAIMS_FOR_ACCESS_TOKEN;
import static com.backend.support.fixture.MemberFixture.NICKNAME;
import static com.backend.support.fixture.PostFixture.CONTENT;
import static com.backend.support.fixture.PostFixture.INVALID_POST_WRITE_REQUEST;
import static com.backend.support.fixture.PostFixture.POST_DETAIL_RESPONSE;
import static com.backend.support.fixture.PostFixture.TITLE;
import static com.backend.support.fixture.PostFixture.VALID_POST_WRITE_REQUEST;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@Import(SecurityConfig.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("게시글을 작성한다.")
    void write_request_test() throws Exception {
        willDoNothing().given(jwtUtil).validatedToken(anyString());
        given(jwtUtil.getPayload(anyString())).willReturn(CLAIMS_FOR_ACCESS_TOKEN);
        willDoNothing().given(postService).write(anyString(), any(PostWriteRequest.class));

        mockMvc.perform(post("/api/post/write")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(VALID_POST_WRITE_REQUEST)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우 게시글 작성에 실패한다.")
    void write_request_invalid_input_value_test() throws Exception {
        ErrorType errorType = ErrorType.INVALID_INPUT_VALUE;

        given(jwtUtil.getPayload(anyString())).willReturn(CLAIMS_FOR_ACCESS_TOKEN);

        mockMvc.perform(post("/api/post/write")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(INVALID_POST_WRITE_REQUEST)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("게시글 목록을 조회한다.")
    void list_request_test() throws Exception {
        List<PostItem> postItems = Arrays.asList(
                new PostItem(4L, "title4", "writer4", LocalDateTime.now()),
                new PostItem(3L, "title3", "writer3", LocalDateTime.now()),
                new PostItem(2L, "title2", "writer2", LocalDateTime.now()),
                new PostItem(1L, "title1", "writer1", LocalDateTime.now())
        );
        PostListResponse response = new PostListResponse(postItems, 1, true, true);
        given(postService.list(anyInt())).willReturn(response);

        mockMvc.perform(get("/api/post/list")
                        .param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts[0].postNum").value(4L))
                .andExpect(jsonPath("$.posts[0].title").value("title4"))
                .andExpect(jsonPath("$.posts[0].writer").value("writer4"))
                .andExpect(jsonPath("$.curPage").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    @DisplayName("게시글을 상세 조회한다.")
    void detail_request_test() throws Exception {
        given(postService.detail(anyLong())).willReturn(POST_DETAIL_RESPONSE);

        mockMvc.perform(get("/api/post/detail/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postNum").value(1L))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.writer").value(NICKNAME))
                .andExpect(jsonPath("$.content").value(CONTENT));
    }

    @Test
    @DisplayName("게시글을 찾을 수 없을시 상세 조회에 실패한다.")
    void detail_request_not_found_post_test() throws Exception {
        ErrorType errorType = ErrorType.NOT_FOUND_POST;

        willThrow(new NotFoundPostException(ErrorType.NOT_FOUND_POST)).given(postService).detail(anyLong());

        mockMvc.perform(get("/api/post/detail/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

}