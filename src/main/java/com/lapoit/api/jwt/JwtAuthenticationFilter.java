package com.lapoit.api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    // 나중에 Redis 블랙리스트 쓰면 TokenRepository 주입해서 여기서 체크하면 됨.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

//        System.out.println("[JWT] uri=" + request.getRequestURI());
//        System.out.println("[JWT] authorization=" + request.getHeader("Authorization"));
//        System.out.println("[JWT] tokenNull=" + (token == null));

        boolean valid = token != null && jwtTokenProvider.validateToken(token);
//        System.out.println("[JWT] valid=" + valid);

        if (valid) {
            try {
//                System.out.println("[JWT] userId=" + jwtTokenProvider.getUserId(token));
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
//                System.out.println("[JWT] auth=" + authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                e.printStackTrace(); // loadUserByUsername 실패 등 잡아냄
            }
        }

        filterChain.doFilter(request, response);
    }
}