package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.RoleRequest;
import com.dotcare.backend.dto.SignupRequest;
import com.dotcare.backend.entity.Role;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.RoleRepository;
import com.dotcare.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RolesController {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RolesController(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/addRole")
    public String addDoctor(@RequestBody RoleRequest RoleRequest, HttpServletRequest request) {

        User user = userRepository.findByUsername(RoleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_" + RoleRequest.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        System.out.println("Role name is" + userRole.getName());
//        check is that already doctor
        if (user.getRoles().contains(userRole)) {
            return "User is already a " + RoleRequest.getRole();
        }
        roles.add(userRole);

        user.setRoles(roles);
        System.out.println("Role name is" + user.getRoles());
        userRepository.save(user);

        return "MOH role added successfully!";
    }

    //make for delete role

    @PostMapping("/deleteRole")
    public String deleteRole(@RequestBody RoleRequest RoleRequest, HttpServletRequest request) {

        User user = userRepository.findByUsername(RoleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_" + RoleRequest.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        System.out.println("Role name is" + userRole.getName());
        return "";
    }
}