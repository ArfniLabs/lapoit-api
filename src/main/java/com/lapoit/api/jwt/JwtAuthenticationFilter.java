package com.lapoit.api.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lapoit.api.dto.ApiResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;
    // 나중에 Redis 블랙리스트 쓰면 TokenRepository 주입해서 여기서 체크하면 됨.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);
        if (token == null && request.getRequestURI().startsWith("/api/v1/sse/subscribe")) {
            String queryToken = request.getParameter("token");
            if (queryToken != null && !queryToken.isBlank()) {
                token = queryToken.startsWith("Bearer ") ? queryToken.substring(7) : queryToken;
            }
        }

//        System.out.println("[JWT] uri=" + request.getRequestURI());
//        System.out.println("[JWT] authorization=" + request.getHeader("Authorization"));
//        System.out.println("[JWT] tokenNull=" + (token == null));

        boolean valid = token != null && jwtTokenProvider.validateToken(token);
//        System.out.println("[JWT] valid=" + valid);

        if (valid) {
            try {

                String userId=jwtTokenProvider.getUserId(token);
                long issuedAtMillis = jwtTokenProvider.getIssuedAtMillis(token);
                String lastLogoutAtStr = redisTemplate.opsForValue().get("LO:" + userId);

                if (lastLogoutAtStr != null) {
                    long lastLogoutAt = Long.parseLong(lastLogoutAtStr);
                    if (issuedAtMillis < lastLogoutAt) {
                        // 로그아웃 이전 토큰 => 무효
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");

                        ApiResponseDto<Void> body =
                                ApiResponseDto.fail("AUTH-401", "로그아웃된 토큰입니다.");

                        response.getWriter().write(
                                new ObjectMapper().writeValueAsString(body)
                        );

                        return;
                    }
                }



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
