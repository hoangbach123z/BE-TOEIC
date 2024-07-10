package com.backend.spring.controller;

import com.backend.spring.data.JwtResponse;
import com.backend.spring.data.TokenRefreshResponse;
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
import jakarta.validation.Valid;
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
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;



    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Kiểm tra nếu tài khoản chưa được kích hoạt
            if (userDetails.getIsActive() == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực."));
            }

            // Kiểm tra nếu tài khoản chưa được kích hoạt
            if (userDetails.getStatus() == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Tài khoản đã bị khóa."));
            }

            // Tạo Access Token và Refresh Token
            String jwt = jwtUtils.generateJwtToken(userDetails);
            String refreshToken = refreshTokenService.createRefreshToken(userDetails.getId()).getToken();

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Tính toán thời gian hết hạn của Access Token và Refresh Token
            long jwtExpirationTime = jwtUtils.getJwtExpirationMs();
            long refreshTokenExpirationTime = refreshTokenService.getRefreshTokenDurationMs();

            // Trả về response với thông tin thời gian hết hạn dưới dạng Date
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    refreshToken,
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
                    jwtExpirationTime, refreshTokenExpirationTime));
        } catch (BadCredentialsException e) {
            // Xử lý khi tên đăng nhập hoặc mật khẩu không đúng
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Tên đăng nhập hoặc mật khẩu không đúng."));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));
        }
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email đã được sử dụng!"));
        }
        if (userRepository.existsByPhoneNumber(signUpDTO.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(new MessageResponse("SĐT đã được sử dụng!"));
        }
        // Create new user's account
        User user = new User(
                signUpDTO.getName(),
                signUpDTO.getUsername(),
                signUpDTO.getEmail(),
                encoder.encode(signUpDTO.getPassword()),
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
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_LEARNER)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        // Gửi email xác thực
        String subject = "Xác thực tài khoản";
        // Đọc nội dung của file template
//        String templateContent = loadVerificationEmailTemplate(verificationCode);
        // Gửi email sử dụng template
//        emailService.sendEmail(signUpDto.getEmail(), subject, templateContent);

        return ResponseEntity.ok(new MessageResponse("Đăng kí người dùng dành công"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername(), user.getId());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token không có trong CSDL!"));
    }
}

