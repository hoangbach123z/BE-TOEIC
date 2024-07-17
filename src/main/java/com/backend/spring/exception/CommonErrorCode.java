package com.backend.spring.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements AbstractError {
  SUCCESSFUL(
      0,
      "Successful",
      HttpStatus.OK),
  BAD_REQUEST(
      400,
      "Server cannot or will not process the request due to something that is to be a client error.",
      HttpStatus.BAD_REQUEST),
  INVALID_FIELD(
      400,
      "%s",
      HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(
      401,
      "There was an error during authentication, requires login again.",
      HttpStatus.UNAUTHORIZED),
  FORBIDDEN(403, "You do not have permission to perform this operation.", HttpStatus.FORBIDDEN),
  NOT_FOUND(404, "No handler found for %s %s", HttpStatus.NOT_FOUND),
  METHOD_NOT_ALLOWED(
      405,
      "Request method is known by the server but is not supported by the target resource",
      HttpStatus.METHOD_NOT_ALLOWED),
  DATA_INTEGRITY_VIOLATION(
      409,
      "An attempt to insert or update data results in violation of an integrity constraint.",
      HttpStatus.CONFLICT),
  MISSING_REQUEST_PARAMETER(400, "Required %s parameter is not present", HttpStatus.BAD_REQUEST),
  UNSUPPORTED_MEDIA_TYPE(
      415,
      "The server refuses to accept the request because the payload format is in an unsupported format",
      HttpStatus.UNSUPPORTED_MEDIA_TYPE),
  MEDIA_TYPE_NOT_ACCEPTABLE(
      415,
      "The request handler cannot generate a response that is acceptable by the client",
      HttpStatus.UNSUPPORTED_MEDIA_TYPE),
  ARGUMENT_TYPE_MISMATCH(
      400, "Required type %s parameter '%s' is not match", HttpStatus.BAD_REQUEST),
  ARGUMENT_NOT_VALID(400, "Invalid parameter %s ", HttpStatus.BAD_REQUEST),
  ENTITY_NOT_FOUND(404, "Object no longer exists in the database", HttpStatus.NOT_FOUND),
  INTERNAL_SERVER_ERROR(
      500,
      "There was an error processing the request, please contact the administrator!",
      HttpStatus.INTERNAL_SERVER_ERROR),
  ACCESS_TOKEN_INVALID(
      401,
      "Access token invalid, create a new access token or login again!",
      HttpStatus.UNAUTHORIZED),
  ACCESS_TOKEN_EXPIRED(
      401,
      "Access token expired, create a new access token or login again!",
      HttpStatus.UNAUTHORIZED),
  REFRESH_TOKEN_INVALID(
      401, "The refresh token invalid, requires login again!", HttpStatus.UNAUTHORIZED),
  REFRESH_TOKEN_EXPIRED(
      401, "The refresh token expired, requires login again!", HttpStatus.UNAUTHORIZED),
  EXECUTE_THIRTY_SERVICE_ERROR(
      400, "An error occurred while executing the 3rd party api!", HttpStatus.BAD_REQUEST),
  // custom
  EXISTED_USER(10000,"Username đã tồn tại",HttpStatus.BAD_REQUEST),
  USED_EMAIL(10001,"Email đã được sử dụng",HttpStatus.BAD_REQUEST),
  EXISTED_PHONE_NUMBER(10002,"SĐT đã được sử dụng",HttpStatus.BAD_REQUEST),
  ACCOUNT_INACTIVE(10003,"Tài khoản chưa được kích hoạt. Vui lòng kiểm tra email để xác thực.",HttpStatus.BAD_REQUEST),
  BLOCKED_ACCOUNT(10004,"Tài khoản đã bị khóa",HttpStatus.BAD_REQUEST),
  ROLE_NOT_FOUND(10005,"Không tìm thấy vai trò",HttpStatus.BAD_REQUEST),
  SIGNUP_SUCCESSFUL(10006,"Đăng ký tài khoản thành công",HttpStatus.OK),
  USER_OR_PASS_INCORRECT(10007,"Tài khoản hoặc mật khẩu không chính xác",HttpStatus.UNAUTHORIZED),
  TOKEN_NOT_FOUND(10008,"Refresh token không tồn tại",HttpStatus.FORBIDDEN),
  USER_NOT_FOUND(10009,"Tài Khoản không tồn tại",HttpStatus.NOT_FOUND),
  OLD_PASSWORD_INCORRECT(10010,"Mật khẩu cũ không chính xác",HttpStatus.BAD_REQUEST),
  EXISTED_EXAM_NAME(10011,"Exam Name đã tồn tại",HttpStatus.BAD_REQUEST),
  NOT_EXISTED_EXAM_ID(10012,"Exam ID không tồn tại",HttpStatus.BAD_REQUEST),
  INVALID_EXAM_TYPE(10013,"Exam TYPE không hợp lệ",HttpStatus.BAD_REQUEST),




  ;

  private final int code;

  private final String message;

  private final HttpStatus httpStatus;

  CommonErrorCode(int code, String message, HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
