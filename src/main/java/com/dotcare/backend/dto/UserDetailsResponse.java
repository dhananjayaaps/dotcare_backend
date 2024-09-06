package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {
    private String username;
    private String email;
    private String name;
    private boolean success;
    public UserDetailsResponse() {
    }

    public UserDetailsResponse(String username, String name, String email, boolean success) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.success = success;
    }

}
