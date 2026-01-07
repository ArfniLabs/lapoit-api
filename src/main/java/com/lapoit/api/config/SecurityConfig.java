package com.lapoit.api.config;

import com.lapoit.api.jwt.CustomUserDetailsService;
import com.lapoit.api.jwt.JwtAuthenticationFilter;
import com.lapoit.api.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')") 같은 어노테이션 쓰려고 활성화
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final StringRedisTemplate redisTemplate;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // JWT 이므로 CSRF, 폼 로그인, HTTP Basic은 끔
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션을 쓰지 않고, 매 요청마다 JWT로만 인증
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청별 인가(권한) 설정
                .authorizeHttpRequests(auth -> auth
                        // 로그인/회원가입, Swagger, 기타 공개 API
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/api/v1/auth/signup",
                                "/api/v1/auth/check-id",
                                "/api/v1/auth/check-nickname",
                                "/api/v1/auth/check-phone",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/find-id",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        // 그 외 /api/** 는 전부 인증 필요
                        .requestMatchers("/api/v1/auth/logout").authenticated()
                        .requestMatchers("/api/v1/history/**").authenticated()
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").authenticated()
                        //hasAuthority("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        // 나머지는 일단 허용(필요하면 .authenticated() 로 바꿔도 됨)
                        .anyRequest().permitAll()
                )

                // CORS 설정은 WebConfig에서 따로
//                .cors(Customizer.withDefaults());
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        //  JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 끼워 넣기
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider,redisTemplate),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    // 비밀번호 암호화를 위한 PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager: 로그인 시 사용할 수 있음 (나중에 AuthService에서 주입받아 사용 가능)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    // DaoAuthenticationProvider: UserDetailsService + PasswordEncoder 묶어둔 제공자
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // 🔥 핵심
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}