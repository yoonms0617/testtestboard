package com.backend.member.controller;

import com.backend.global.error.exception.ErrorType;
import com.backend.global.security.config.SecurityConfig;
import com.backend.member.dto.MemberSignupRequest;
import com.backend.member.exception.DuplicateNicknameException;
import com.backend.member.exception.DuplicateUsernameException;
import com.backend.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
@Import(SecurityConfig.class)
class MemberControllerTest {

    private static final String NICKNAME = "민수쿤";
    private static final String USERNAME = "yoonms0617";
    private static final String PASSWORD = "1q2w3e4r!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("정상적으로 회원가입에 성공한다.")
    void signup_request_test() throws Exception {
        MemberSignupRequest request = new MemberSignupRequest(NICKNAME, USERNAME, PASSWORD);

        willDoNothing().given(memberService).signup(any(MemberSignupRequest.class));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/member/signup")
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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/member/signup")
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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/member/signup")
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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/member/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)));

        resultActions
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorType.getCode()))
                .andExpect(jsonPath("$.status").value(errorType.getStatus()))
                .andExpect(jsonPath("$.message").value(errorType.getMessage()));
    }

}