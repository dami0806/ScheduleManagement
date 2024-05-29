package com.sparta.scheduleapp.auth.config;

import com.sparta.scheduleapp.auth.filter.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.swing.*;

@Configuration
@EnableWebSecurity
/**
 * SecurityConfig- Spring Security 설정
  */
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * SecurityFilterChain: 보안 설정을 기반으로 SecurityFilterChain 생성
     * @param http : HttpSecurity객체로, CSRF 비활성화, 세션 관리, 요청 권한, 필터 추가
     * @return SecurityFilterChain객체 생성(http 요청의 보안 규칙이 적용된)
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());  //csrf 비활성화

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 관리를 stateless설정

        // 요청에 대한 권한 설정
        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/", "/index.html", "/login.html", "/signup.html", "/api/auth/**").permitAll() // 특정 경로 접근 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 접근 허용
                        .requestMatchers(HttpMethod.GET, "/api/schedules/**").permitAll() // 일정 조회는 모두 허용

                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        /**
         * jwtAuthenticationFilter 후에 password 필터 지정
         */
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
