package com.backend.spring.controller;

import com.backend.spring.data.ResponseData;
import com.backend.spring.entity.User;
import com.backend.spring.exception.MessageResponse;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.response.JwtResponse;
import com.backend.spring.payload.response.TokenRefreshResponse;
import com.backend.spring.dto.LoginDTO;
import com.backend.spring.dto.SignUpDTO;
import com.backend.spring.dto.TokenRefreshDTO;
import com.backend.spring.security.service.RefreshTokenService;
import com.backend.spring.security.service.UserDetailsImpl;
import com.backend.spring.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final IUserService iUserService;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Operation(summary = "API đăng nhập")
    @PostMapping("/signin")
    public ResponseEntity<?>  authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
    return iUserService.authenticateUser(loginDTO);
    }

    @Operation(summary = "API đăng ký")
    @PostMapping("/signup")
    public ResponseData<User>  registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
    return iUserService.registerUser(signUpDTO);
    }
    @Operation(summary = "API làm mới refresh token")
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshDTO request) {
        return iUserService.refreshtoken(request);
    }
    @Operation(summary = "API đăng xuất")
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (!Objects.equals(userDetails.toString(), "anonymousUser")){
//            String userId = ((UserDetailsImpl)userDetails).getId();
//            refreshTokenService.deleteByUserId(userId);
//        }
        String userID = ((UserDetailsImpl )userDetails).getId();
        refreshTokenService.deleteByUserId(userID);
        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
    }

    @Operation(summary = "API thay đổi mật khẩu")
    @PostMapping("/change-password/{userID}")
    public ResponseData<?> changePassword(@PathVariable String userID, @Valid @RequestBody ChangePasswordRequest request) {
        return iUserService.changePassword(userID,request);
    }

    @Operation(summary = "API lấy danh sách learner")
    @GetMapping("/leaners")
    public ResponseData<?> getListLearners() {
        return iUserService.getLearner();
    }

    @Operation(summary = "API count danh sách learner")
    @GetMapping("/leaners/count")
    public ResponseData<Long> countLearners() {
        return iUserService.countLearner();
    }

    @Operation(summary = "API chi tiết user")
    @GetMapping("/user-detail/{userID}")
    public ResponseData<User> getUserDetail(@PathVariable String userID) {
        return iUserService.getUserDetail(userID);
    }



}

