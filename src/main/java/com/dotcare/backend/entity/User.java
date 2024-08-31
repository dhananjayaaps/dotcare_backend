package com.dotcare.backend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private String first_name;
    private String last_name;
    private String nic;
    private String phoneNumber;
    private Boolean marketing_accept;
    private Boolean restricted;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String username, String encode, String email, String first_name, String last_name, String nic, String phoneNumber, Boolean marketing_accept) {
        this.username = username;
        this.password = encode;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nic = nic;
        this.phoneNumber = phoneNumber;
        this.marketing_accept = marketing_accept;
        this.restricted = false;
    }

    public User() {

    }

}