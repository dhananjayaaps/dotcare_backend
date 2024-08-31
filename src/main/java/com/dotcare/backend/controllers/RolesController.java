package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.ApiResponse;
import com.dotcare.backend.dto.RoleRequest;
import com.dotcare.backend.dto.UserDTO;
import com.dotcare.backend.entity.Role;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.RoleRepository;
import com.dotcare.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String addDoctor(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {

        User user = userRepository.findByUsername(roleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Role userRole = roleRepository.findByName("ROLE_" + roleRequest.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        if (user.getRoles().contains(userRole)) {
            return "User is already a " + roleRequest.getRole();
        }

        user.getRoles().add(userRole);
        userRepository.save(user);
        return roleRequest.getRole() + " role added successfully!";
    }


    //make for delete role
    @PostMapping("/deleteRole")
    public String deleteDoctor(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {

        User user = userRepository.findByUsername(roleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Role userRole = roleRepository.findByName("ROLE_" + roleRequest.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        if (!user.getRoles().contains(userRole)) {
            return "User is not a " + roleRequest.getRole();
        }

        user.getRoles().remove(userRole);
        userRepository.save(user);
        return roleRequest.getRole() + " role removed successfully!";
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDetails(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        User user = userRepository.findByUsername(roleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        UserDTO userDTO = new UserDTO(user.getFirst_name() + " " + user.getLast_name(), user.getNic(),
                user.getUsername(), user.getEmail(), user.getRoles().toString());

        return ResponseEntity.ok(new ApiResponse<>(true, "User details are available.", userDTO));
    }
}