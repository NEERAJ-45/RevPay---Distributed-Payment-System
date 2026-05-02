package com.neeraj.upi.user.config;

import com.neeraj.upi.user.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // TODO:
        // 1. Read Authorization header
        // 2. If starts with "Bearer ", extract token
        // 3. If jwtService.isTokenValid(token), extract userId, set SecurityContextHolder authentication
        // 4. Always call filterChain.doFilter(request, response)
        filterChain.doFilter(request, response);
    }
}
