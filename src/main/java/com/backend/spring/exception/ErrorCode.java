package com.backend.spring.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements AbstractError{
  EXISTED_USER(10000,"Username đã tồn tại",HttpStatus.BAD_REQUEST),
  USED_EMAIL(10001,"Email đã được sử dụng",HttpStatus.BAD_REQUEST),
  EXISTED_PHONE_NUMBER(10002,"SĐT đã được sử dụng",HttpStatus.BAD_REQUEST),
  ACCOUNT_INACTIVE(10003,"Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực.",HttpStatus.BAD_REQUEST),
  BLOCKED_ACCOUNT(10004,"Tài khoản đã bị khóa",HttpStatus.BAD_REQUEST),
  ROLE_NOT_FOUND(10005,"Không tìm thấy vai trò",HttpStatus.BAD_REQUEST),
  SIGNUP_SUCCESSFUL(10006,"Đăng ký tài khoản thành công",HttpStatus.OK),
  USER_OR_PASS_INCORRECT(10007,"Tài khoản hoặc mật khẩu không chính xác",HttpStatus.UNAUTHORIZED),
  TOKEN_NOT_FOUND(10008,"Refresh token không tồn tại",HttpStatus.FORBIDDEN)
  ;
  private final int code;
  private final String message;
  private final  HttpStatus httpStatus;

  @Override
  public String getMessage() {
    return message;
  }

  ErrorCode(int code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
