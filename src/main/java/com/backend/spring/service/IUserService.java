package com.backend.spring.service;

import com.backend.spring.dto.LoginDTO;
import com.backend.spring.dto.SignUpDTO;
import com.backend.spring.dto.TokenRefreshDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {
  ResponseEntity<?> authenticateUser( LoginDTO loginDTO);

  ResponseEntity<?> registerUser(SignUpDTO signUpDTO);

  ResponseEntity<?> refreshtoken(TokenRefreshDTO request);
}
