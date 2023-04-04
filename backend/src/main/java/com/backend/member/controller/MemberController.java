package com.backend.member.controller;

import com.backend.member.dto.MemberSignupRequest;
import com.backend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.ok().build();
    }

}
