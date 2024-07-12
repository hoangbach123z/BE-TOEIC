package com.backend.spring.controller;

import com.backend.spring.payload.response.JwtResponse;
import com.backend.spring.payload.response.TokenRefreshResponse;
import com.backend.spring.dto.LoginDTO;
import com.backend.spring.dto.SignUpDTO;
import com.backend.spring.dto.TokenRefreshDTO;
import com.backend.spring.entity.ERole;
import com.backend.spring.entity.RefreshToken;
import com.backend.spring.entity.Role;
import com.backend.spring.entity.User;
import com.backend.spring.exception.MessageResponse;
import com.backend.spring.exception.TokenRefreshException;
import com.backend.spring.repository.RoleRepository;
import com.backend.spring.repository.UserRepository;
import com.backend.spring.security.jwt.JwtUtils;
import com.backend.spring.security.service.RefreshTokenService;
import com.backend.spring.security.service.UserDetailsImpl;
import com.backend.spring.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final IUserService iUserService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshDTO request) {

}

