package com.backend.spring.data;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class BodyRequest<T> {

  private String encryptData;

  private T rawData;
}
