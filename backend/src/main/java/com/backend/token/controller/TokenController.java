package com.backend.token.controller;

import com.backend.token.dto.TokenResponse;
import com.backend.token.service.TokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private static final String EMPTY_STRING = "";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reIssueAccessToken(HttpServletRequest request) {
        String refreshtoken = extractToken(request);
        TokenResponse response = tokenService.reIssueAccessToken(refreshtoken);
        return ResponseEntity.ok().body(response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()).trim();
        }
        return EMPTY_STRING;
    }

}
