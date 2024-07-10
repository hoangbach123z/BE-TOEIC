package com.backend.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRefreshDTO {
    @NotBlank
    private String refreshToken;
}

