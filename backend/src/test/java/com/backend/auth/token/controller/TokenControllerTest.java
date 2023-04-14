package com.backend.auth.token.controller;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.config.SecurityConfig;
import com.backend.global.security.exception.InvalidTokenException;
import com.backend.global.security.util.JwtUtil;
import com.backend.token.controller.TokenController;
import com.backend.token.service.TokenService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static com.backend.support.fixture.AuthFixture.TOKEN_RESPONSE;
import static com.backend.support.fixture.JwtFixture.ACCESS_TOKEN;
import static com.backend.support.fixture.JwtFixture.INVALID_REFRESH_TOKEN;
import static com.backend.support.fixture.JwtFixture.REFRESH_TOKEN;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TokenController.class)
@Import(SecurityConfig.class)
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("토큰을 재발급 받는다.")
    void token_reissue_request_test() throws Exception {
        given(tokenService.reIssueAccessToken(anyString())).willReturn(TOKEN_RESPONSE);

        mockMvc.perform(post("/api/token/reissue")
                        .header("Authorization", "Bearer " + REFRESH_TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN));
    }

    @Test
    @DisplayName("유효하지 않는 토큰일 경우 재발급에 실패한다.")
    void token_reissue_invalid_token_request_test() throws Exception {
        ErrorType errorType = ErrorType.INVALID_TOKEN;

        given(tokenService.reIssueAccessToken(anyString())).willThrow(new InvalidTokenException(ErrorType.INVALID_TOKEN.getCode()));

        mockMvc.perform(post("/api/token/reissue")
                        .header("Authorization", "Bearer " + INVALID_REFRESH_TOKEN)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

}
