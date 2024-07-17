package com.backend.spring.exception;

import com.backend.spring.data.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultExceptionHandler {


  @ExceptionHandler({BaseException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> businessExceptionHandler(BaseException ex){
    log.error("Business Error: {} ",ex.toString(),ex);
    return ResponseUtils.error(ex.getCode(),ex.getMessage(),ex.getHttpStatus());
  }
}

