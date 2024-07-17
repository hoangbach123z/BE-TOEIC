package com.backend.spring.service.impl;

import com.backend.spring.data.ResponseData;
import com.backend.spring.dto.LoginDTO;
import com.backend.spring.dto.SignUpDTO;
import com.backend.spring.dto.TokenRefreshDTO;
import com.backend.spring.entity.ERole;
import com.backend.spring.entity.RefreshToken;
import com.backend.spring.entity.Role;
import com.backend.spring.entity.User;
import com.backend.spring.exception.BaseException;
import com.backend.spring.exception.CommonErrorCode;
import com.backend.spring.exception.MessageResponse;
import com.backend.spring.payload.request.ChangePasswordRequest;
import com.backend.spring.payload.response.JwtResponse;
import com.backend.spring.payload.response.TokenRefreshResponse;
import com.backend.spring.repository.RoleRepository;
import com.backend.spring.repository.UserRepository;
import com.backend.spring.security.jwt.JwtUtils;
import com.backend.spring.security.service.RefreshTokenService;
import com.backend.spring.security.service.UserDetailsImpl;
import com.backend.spring.service.IUserService;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Override
  public ResponseEntity<?> authenticateUser(LoginDTO loginDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      // Kiểm tra nếu tài khoản chưa được kích hoạt
      if (userDetails.getIsActive() == 0) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//            .body(new MessageResponse("Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực."));
        throw new BaseException(CommonErrorCode.ACCOUNT_INACTIVE);
      }

      // Kiểm tra nếu tài khoản chưa được kích hoạt
      if (userDetails.getStatus() == 0) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//            .body(new MessageResponse("Tài khoản đã bị khóa."));
        throw new BaseException(CommonErrorCode.BLOCKED_ACCOUNT);
      }

      // Tạo Access Token và Refresh Token
      String jwt = jwtUtils.generateJwtToken(userDetails);
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());

      // Tính toán thời gian hết hạn của Access Token và Refresh Token
      long jwtExpirationTime = jwtUtils.getJwtExpirationMs();
      long refreshTokenExpirationTime = refreshTokenService.getRefreshTokenDurationMs();

      // Trả về response với thông tin thời gian hết hạn dưới dạng Date
//      ResponseData<JwtResponse> data = new ResponseData<>();
//      JwtResponse jwtResponse = JwtResponse.builder()
//          .accessToken(jwt)
//          .refreshToken(refreshToken.getToken())
//          .id(userDetails.getId())
//          .username(userDetails.getUsername())
//          .email(userDetails.getEmail())
//          .address(userDetails.getAddress())
//          .phoneNumber(userDetails.getPhoneNumber())
//          .gender(userDetails.getGender())
//          .status(userDetails.getStatus())
//          .isActive(userDetails.getIsActive())
//          .verificationCode(userDetails.getVerificationCode())
//          .name(userDetails.getName())
//          .roles(roles)
////          .jwtExpirationTime(jwtExpirationTime)
////          .refreshTokenExpirationTime(refreshTokenExpirationTime)
//          .build();
//
//      data.setData(jwtResponse);
//      return data;
      return ResponseEntity.ok(new JwtResponse(
          jwt,
          refreshToken.getToken(),
          userDetails.getId(),
          userDetails.getUsername(),
          userDetails.getEmail(),
          userDetails.getAddress(),
          userDetails.getPhoneNumber(),
          userDetails.getGender(),
          userDetails.getStatus(),
          userDetails.getIsActive(),
          userDetails.getVerificationCode(),
          userDetails.getName(),
          roles,
          jwtExpirationTime,
          refreshTokenExpirationTime
      ));
    } catch (BadCredentialsException e) {
      // Xử lý khi tên đăng nhập hoặc mật khẩu không đúng
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//          .body(new MessageResponse("Tên đăng nhập hoặc mật khẩu không đúng."));
      throw new BaseException(CommonErrorCode.USER_OR_PASS_INCORRECT);
    }
  }

  @Override
  public ResponseData<User> registerUser(SignUpDTO signUpDTO) {
    ResponseData<User> responseData = new ResponseData<>();
    if (userRepository.existsByUsername(signUpDTO.getUsername())) {
//      return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));
      throw new BaseException(CommonErrorCode.EXISTED_USER);
    }
    if (userRepository.existsByEmail(signUpDTO.getEmail())) {
//      return ResponseEntity.badRequest().body(new MessageResponse("Email đã được sử dụng!"));
      throw new BaseException(CommonErrorCode.USED_EMAIL);
    }
    if (userRepository.existsByPhoneNumber(signUpDTO.getPhoneNumber())) {
//      return ResponseEntity.badRequest().body(new MessageResponse("SĐT đã được sử dụng!"));
      throw new BaseException(CommonErrorCode.EXISTED_PHONE_NUMBER);
    }
    // Create new user's account
    User user = new User(
        signUpDTO.getName(),
        signUpDTO.getUsername(),
        signUpDTO.getEmail(),
        passwordEncoder.encode(signUpDTO.getPassword()),
        signUpDTO.getAddress(),
        signUpDTO.getPhoneNumber(),
        signUpDTO.getGender()
    );
    // Tạo mã xác thực
    String verificationCode = UUID.randomUUID().toString();
    user.setVerificationCode(verificationCode);

    Set<String> strRoles = signUpDTO.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_LEARNER)
          .orElseThrow(() -> new BaseException(CommonErrorCode.ROLE_NOT_FOUND));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new BaseException(CommonErrorCode.ROLE_NOT_FOUND));
            roles.add(adminRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_LEARNER)
                .orElseThrow(() -> new BaseException(CommonErrorCode.ROLE_NOT_FOUND));
            roles.add(userRole);
        }
      });
    }
    user.setRoles(roles);
    userRepository.save(user);


//    responseData.setData(user);
    responseData.setMessage("Tạo tài khoản khoản thành công");
    responseData.setCode(200);
    // Gửi email xác thực
    String subject = "Xác thực tài khoản";
    // Đọc nội dung của file template
//        String templateContent = loadVerificationEmailTemplate(verificationCode);
    // Gửi email sử dụng template
//        emailService.sendEmail(signUpDto.getEmail(), subject, templateContent);

//    return ResponseEntity.ok(new BaseException(ErrorCode.SIGNUP_SUCCESSFUL));
    return responseData;

  }

  @Override
  public ResponseEntity<?> refreshtoken(TokenRefreshDTO request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new BaseException(CommonErrorCode.TOKEN_NOT_FOUND));
  }

  @Override
  public ResponseData<?> changePassword(String userID,ChangePasswordRequest request) {
    ResponseData<?> responseData = new ResponseData<>();
    try {
      User existingUser = userRepository.findById(userID);
      if(existingUser != null){
        if (passwordEncoder.matches(request.getOldPassword(),existingUser.getPassword())){
          existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
          userRepository.save(existingUser);
          responseData.setMessage("Đổi mật khẩu thành công");
          responseData.setCode(200);
          return responseData;
        }else {
          throw new BaseException(CommonErrorCode.OLD_PASSWORD_INCORRECT);
        }
      }
      else {
        throw new BaseException(CommonErrorCode.USER_NOT_FOUND);
      }
    } catch (Exception e) {
      // Xử lý các ngoại lệ khác nếu cần
      throw new BaseException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

  }

  @Override
  public ResponseData<List<User>> getLearner() {
    ResponseData<List<User>>responseData = new ResponseData<>();
    List<User> learners = userRepository.findByRoles_Name(ERole.ROLE_LEARNER);
    responseData.setData(learners);
    return responseData;
  }

  @Override
  public ResponseData<Long> countLearner() {
    ResponseData<Long>responseData = new ResponseData<>();
    long countLearners = userRepository.countByRoles_Name(ERole.ROLE_LEARNER);
    responseData.setData(countLearners);
    return responseData;
  }

  @Override
  public ResponseData<User> getUserDetail(String userId) {
    ResponseData<User> responseData = new ResponseData<>();
    User user = userRepository.findById(userId);
    if(Objects.isNull(user)){
      throw new BaseException(CommonErrorCode.USER_NOT_FOUND) ;
    }
    responseData.setData(user);
    return responseData;
  }

  @Override
  public ResponseData<?> updateProfile() {
    return null;
  }

  @Override
  public ResponseData getUserDetailByUsername(String username) {
    return null;
  }

//  @Override
//  public ResponseData<?> logoutUser() {
//    ResponseData<?> data = new ResponseData<>();
//    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    String userId = userDetails.getId();
//    refreshTokenService.deleteByUserId(userId);
//    data.setMessage("Đăng xuất thành công");
//    return data;
//  }
}
