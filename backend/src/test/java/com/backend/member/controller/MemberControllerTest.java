package com.backend.member.controller;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.config.SecurityConfig;
import com.backend.global.security.exception.InvalidTokenException;
import com.backend.global.security.util.JwtUtil;
import com.backend.member.dto.MemberSignupRequest;
import com.backend.member.exception.DuplicateNicknameException;
import com.backend.member.exception.DuplicateUsernameException;
import com.backend.member.service.MemberService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static com.backend.support.fixture.AuthFixture.LOGIN_MEMBER;
import static com.backend.support.fixture.JwtFixture.ACCESS_TOKEN;
import static com.backend.support.fixture.JwtFixture.REFRESH_TOKEN;
import static com.backend.support.fixture.JwtFixture.INVALID_ACCESS_TOKEN;
import static com.backend.support.fixture.JwtFixture.CLAIMS_FOR_ACCESS_TOKEN;
import static com.backend.support.fixture.MemberFixture.MEMBER_PROFILE_RESPONSE;
import static com.backend.support.fixture.MemberFixture.NICKNAME;
import static com.backend.support.fixture.MemberFixture.USERNAME;
import static com.backend.support.fixture.MemberFixture.PLAIN_PASSWORD;
import static com.backend.support.fixture.MemberFixture.VALID_SIGNUP_REQUEST;
import static com.backend.support.fixture.MemberFixture.INVALID_SIGNUP_REQUEST;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

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
    private MemberService memberService;

    @Test
    @DisplayName("정상적으로 회원가입에 성공한다.")
    void signup_request_test() throws Exception {
        willDoNothing().given(memberService).signup(any(MemberSignupRequest.class));

        mockMvc.perform(post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(VALID_SIGNUP_REQUEST)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우 회원가입에 실패한다.")
    void signup_request_invalid_input_value_test() throws Exception {
        ErrorType errorType = ErrorType.INVALID_INPUT_VALUE;

        willDoNothing().given(memberService).signup(any(MemberSignupRequest.class));

        mockMvc.perform(post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(INVALID_SIGNUP_REQUEST)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("닉네임이 중복되지 않는다.")
    void exists_nickname_request_test() throws Exception {
        willDoNothing().given(memberService).existsNickname(anyString());

        mockMvc.perform(get("/api/member/nickname/exists")
                        .param("nickname", NICKNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Y"));
    }

    @Test
    @DisplayName("아이디가 중복되지 않는다.")
    void exists_username_request_test() throws Exception {
        willDoNothing().given(memberService).existsUsername(anyString());

        mockMvc.perform(get("/api/member/username/exists")
                        .param("username", USERNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Y"));
    }

    @Test
    @DisplayName("닉네임이 중복될시 회원가입에 실패한다.")
    void signup_request_duplicate_nickname_test() throws Exception {
        ErrorType errorType = ErrorType.DUPLICATE_NICKNAME;

        willThrow(new DuplicateNicknameException(ErrorType.DUPLICATE_NICKNAME)).given(memberService)
                .signup(any(MemberSignupRequest.class));

        mockMvc.perform(post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(VALID_SIGNUP_REQUEST)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("아이디가 중복될시 회원가입에 실패한다.")
    void signup_request_duplicate_username_test() throws Exception {
        ErrorType errorType = ErrorType.DUPLICATE_USERNAME;

        willThrow(new DuplicateUsernameException(ErrorType.DUPLICATE_USERNAME)).given(memberService).signup(any(MemberSignupRequest.class));

        mockMvc.perform(post("/api/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(VALID_SIGNUP_REQUEST)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("정상적으로 로그인에 성공하면 Access Token과 Refresh Token을 응답한다.")
    void login_request_test() throws Exception {
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(LOGIN_MEMBER);
        given(jwtUtil.createAccessToken(anyLong(), anyString(), anyString(), anyString())).willReturn(ACCESS_TOKEN);
        given(jwtUtil.createRefreshToken(anyLong(), anyString(), anyString(), anyString())).willReturn(REFRESH_TOKEN);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "yoonms0617")
                        .param("password", "1q2w3e4r!"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(NICKNAME))
                .andExpect(jsonPath("$.token.accessToken").value(ACCESS_TOKEN))
                .andExpect(jsonPath("$.token.refreshToken").value(REFRESH_TOKEN));
    }

    @Test
    @DisplayName("잘못된 아이디 또는 비밀번호를 입력하면 로그인에 실패한다.")
    void login_request_invalid_username_password_test() throws Exception {
        ErrorType errorType = ErrorType.BAD_CREDENTIALS;

        willThrow(UsernameNotFoundException.class).given(userDetailsService).loadUserByUsername(anyString());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", USERNAME)
                        .param("password", PLAIN_PASSWORD))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("정상적으로 회원 정보를 조회한다.")
    void profile_request_test() throws Exception {
        given(userDetailsService.loadUserByUsername(anyString())).willReturn(LOGIN_MEMBER);
        given(jwtUtil.createAccessToken(anyLong(), anyString(), anyString(), anyString())).willReturn(ACCESS_TOKEN);
        given(jwtUtil.getPayload(anyString())).willReturn(CLAIMS_FOR_ACCESS_TOKEN);
        given(memberService.profile(anyString())).willReturn(MEMBER_PROFILE_RESPONSE);

        mockMvc.perform(get("/api/member/profile")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(NICKNAME))
                .andExpect(jsonPath("$.username").value(USERNAME));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 회원 정보를 조회할시 실패한다.")
    void profile_invalid_token_request_test() throws Exception {
        ErrorType errorType = ErrorType.INVALID_TOKEN;

        willThrow(new InvalidTokenException(ErrorType.INVALID_TOKEN.getCode())).given(jwtUtil).validatedToken(anyString());

        mockMvc.perform(get("/api/member/token")
                        .header("Authorization", "Bearer " + INVALID_ACCESS_TOKEN)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

}