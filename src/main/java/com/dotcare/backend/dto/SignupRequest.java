package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class SignupRequest {
    private String first_name;
    private String last_name;
    private String email;
    private String nic;
    private String password;
    private String password_confirmation;
    private String phoneNumber;
    private Boolean marketing_accept;
    private String username;
}
