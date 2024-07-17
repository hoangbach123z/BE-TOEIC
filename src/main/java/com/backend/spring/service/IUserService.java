package com.backend.spring.service;

import com.backend.spring.data.ResponseData;
import com.backend.spring.dto.LoginDTO;
import com.backend.spring.dto.SignUpDTO;
import com.backend.spring.dto.TokenRefreshDTO;
import com.backend.spring.entity.User;
import com.backend.spring.payload.request.ChangePasswordRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {
  ResponseEntity<?> authenticateUser( LoginDTO loginDTO);

  ResponseData<User> registerUser(SignUpDTO signUpDTO);

  ResponseEntity<?> refreshtoken(TokenRefreshDTO request);
//  ResponseData<?> logoutUser();
  ResponseData<?> changePassword(String userID,ChangePasswordRequest request);
  ResponseData<List<User>>getLearner();
  ResponseData<Long> countLearner();
  ResponseData<User> getUserDetail(String userId);
  ResponseData<?> updateProfile();
  ResponseData<?> getUserDetailByUsername(String username);
}
