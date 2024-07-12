package com.backend.spring.controller;

import com.backend.spring.data.ResponseData;
import com.backend.spring.payload.response.JwtResponse;
import com.backend.spring.payload.response.TokenRefreshResponse;
import com.backend.spring.dto.LoginDTO;
import com.backend.spring.dto.SignUpDTO;
import com.backend.spring.dto.TokenRefreshDTO;
import com.backend.spring.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final IUserService iUserService;

    @PostMapping("/signin")
    public ResponseData<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
    return iUserService.authenticateUser(loginDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
    return iUserService.registerUser(signUpDTO);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshDTO request) {
        return iUserService.refreshtoken(request);
    }

}

