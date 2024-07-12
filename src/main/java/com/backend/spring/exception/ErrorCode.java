package com.backend.spring.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements AbstractError{
  EXISTED_USER(10000,"Username đã tồn tại",HttpStatus.BAD_REQUEST),
  USED_EMAIL(10001,"Email đã được sử dụng",HttpStatus.BAD_REQUEST),
  EXISTED_PHONE_NUMBER(10002,"SĐT đã được sử dụng",HttpStatus.BAD_REQUEST),
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
