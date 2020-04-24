package com.gnomon.api.payloads.requests;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String nameOrEmail;

    @NotBlank
    private String password;
}
