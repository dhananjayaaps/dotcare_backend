package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String name;
    private String nic;
    private String username;
    private String email;
    private String role;

    public UserDTO(String name, String nic, String username, String email, String role) {
        this.name = name;
        this.nic = nic;
        this.username = username;
        this.email = email;
        this.role = role;
    }

}
