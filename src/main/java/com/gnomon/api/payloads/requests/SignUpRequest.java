package com.gnomon.api.payloads.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    @NotBlank
    @Size(min= 3, max = 64)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max=64)
    private String password;
}
