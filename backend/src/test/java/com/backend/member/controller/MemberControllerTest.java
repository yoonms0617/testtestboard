package com.backend.member.controller;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.config.SecurityConfig;
import com.backend.global.security.dto.LoginMember;
import com.backend.global.security.util.JwtUtil;
import com.backend.member.dto.MemberSignupRequest;
import com.backend.member.exception.DuplicateNicknameException;
import com.backend.member.exception.DuplicateUsernameException;
import com.backend.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

    private static final Long ID = 1L;
    private static final String NICKNAME = "민수쿤";
    private static final String USERNAME = "yoonms0617";
    private static final String PASSWORD = "1q2w3e4r!";
    private static final String ROLE = "ROLE_MEMBER";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("정상적으로 회원가입에 성공한다.")
    void signup_request_test() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(NICKNAME, USERNAME, PASSWORD);

        willDoNothing().given(memberService).signup(any(MemberSignupRequest.class));

        ResultActions resultActions = mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우 회원가입에 실패한다.")
    void signup_request_invalid_input_value_test() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest("", "", "");
        ErrorType errorType = ErrorType.INVALID_INPUT_VALUE;

        willDoNothing().given(memberService).signup(any(MemberSignupRequest.class));

        ResultActions resultActions = mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("닉네임이 중복될시 회원가입에 실패한다.")
    void signup_request_duplicate_nickname_test() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(NICKNAME, USERNAME, PASSWORD);
        ErrorType errorType = ErrorType.DUPLICATE_NICKNAME;

        willThrow(new DuplicateNicknameException(ErrorType.DUPLICATE_NICKNAME)).given(memberService).signup(any(MemberSignupRequest.class));

        ResultActions resultActions = mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("아이디가 중복될시 회원가입에 실패한다.")
    void signup_request_duplicate_username_test() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(NICKNAME, USERNAME, PASSWORD);
        ErrorType errorType = ErrorType.DUPLICATE_USERNAME;

        willThrow(new DuplicateUsernameException(ErrorType.DUPLICATE_USERNAME)).given(memberService).signup(any(MemberSignupRequest.class));

        ResultActions resultActions = mockMvc.perform(post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

    @Test
    @DisplayName("정상적으로 로그인에 성공하면 Access Token과 Refresh Token을 응답한다.")
    void login_request_test() throws Exception {
        String encoded = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(PASSWORD);
        LoginMember loginMember = new LoginMember(ID, USERNAME, encoded, ROLE);
        Date iat = new Date();
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(ID))
                .claim("username", USERNAME)
                .claim("role", ROLE)
                .setIssuedAt(iat)
                .setExpiration(new Date(iat.getTime() + 600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(ID))
                .claim("username", USERNAME)
                .claim("role", ROLE)
                .setIssuedAt(iat)
                .setExpiration(new Date(iat.getTime() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        given(userDetailsService.loadUserByUsername(anyString())).willReturn(loginMember);
        given(jwtUtil.createAccessToken(anyLong(), anyString(), anyString())).willReturn(accessToken);
        given(jwtUtil.createRefreshToken(anyLong(), anyString(), anyString())).willReturn(refreshToken);

        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", PASSWORD));

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    @DisplayName("잘못된 아이디 또는 비밀번호를 입력하면 로그인에 실패한다.")
    void login_request_invalid_username_password_test() throws Exception {
        ErrorType errorType = ErrorType.BAD_CREDENTIALS;

        willThrow(UsernameNotFoundException.class).given(userDetailsService).loadUserByUsername(anyString());

        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", USERNAME)
                .param("password", PASSWORD));

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

}