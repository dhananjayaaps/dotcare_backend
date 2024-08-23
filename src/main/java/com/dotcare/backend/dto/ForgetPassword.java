package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPassword {
    private String email;
    private String password;
    private String confirmPassword;
    private String token;
}
