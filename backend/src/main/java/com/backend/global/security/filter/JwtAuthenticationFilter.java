package com.backend.global.security.filter;

import com.backend.global.security.dto.LoginMember;
import com.backend.global.security.util.JwtUtil;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private static final String[] WHITE_LIST = {
            "/api/member/signup", "/api/auth/login", "/api/post/detail", "/api/post/list"
    };

    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            jwtUtil.validatedToken(token);
            Claims payload = jwtUtil.getPayload(token);
            Authentication authentication = createAuth(payload);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            authenticationEntryPoint.commence(request, response, e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (Strings.hasText(header) && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }

    private Authentication createAuth(Claims payload) {
        Long id = Long.valueOf(payload.getSubject());
        String username = String.valueOf(payload.get("username"));
        String role = String.valueOf(payload.get("role"));
        LoginMember loginMember = new LoginMember(id, username, "", role);
        return UsernamePasswordAuthenticationToken.authenticated(loginMember, null, loginMember.getAuthorities());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (String uri : WHITE_LIST) {
            String requestURI = request.getRequestURI();
            if (requestURI.startsWith(uri)) {
                return true;
            }
        }
        return false;
    }

}