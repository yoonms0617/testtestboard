package com.backend.member.controller;

import com.backend.global.security.dto.LoginMember;
import com.backend.member.dto.MemberProfileResponse;
import com.backend.member.dto.MemberSignupRequest;
import com.backend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/nickname/exists")
    public ResponseEntity<String> existsNickname(@RequestParam("nickname") String nickname) {
        memberService.existsNickname(nickname);
        return ResponseEntity.ok().body("Y");
    }

    @GetMapping("/username/exists")
    public ResponseEntity<String> existsUsername(@RequestParam("username") String username) {
        memberService.existsUsername(username);
        return ResponseEntity.ok().body("Y");
    }

    @GetMapping("/profile")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<MemberProfileResponse> profile(@AuthenticationPrincipal LoginMember loginMember) {
        MemberProfileResponse response = memberService.profile(loginMember.getUsername());
        return ResponseEntity.ok().body(response);
    }

}
