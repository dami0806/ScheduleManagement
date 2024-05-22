package com.sparta.scheduleapp.auth.controller;

import com.sparta.scheduleapp.auth.dto.LoginRequestDto;
import com.sparta.scheduleapp.auth.dto.SignupRequestDto;
import com.sparta.scheduleapp.auth.dto.TokenResponseDto;
import com.sparta.scheduleapp.auth.service.UserService;
import com.sparta.scheduleapp.auth.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthRestController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.login(loginRequestDto);
        String refreshToken = jwtUtil.createRefreshToken(loginRequestDto.getUsername());

        TokenResponseDto tokenResponseDto = new TokenResponseDto(token, refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }
}
