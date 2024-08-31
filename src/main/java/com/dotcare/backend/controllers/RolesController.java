package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.ApiResponse;
import com.dotcare.backend.dto.RoleRequest;
import com.dotcare.backend.dto.UserDTO;
import com.dotcare.backend.entity.Role;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.RoleRepository;
import com.dotcare.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ApiResponse<Object>> addDoctor(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        
        User user = userRepository.findByUsername(roleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Role userRole = roleRepository.findByName("ROLE_" + roleRequest.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        if (user.getRoles().contains(userRole)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "User is already a " + roleRequest.getRole(), null));
        }

        user.getRoles().add(userRole);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>(true, roleRequest.getRole() + " role added successfully!", null));
    }


    //make for delete role
    @PostMapping("/deleteRole")
    public ResponseEntity<ApiResponse<Object>> deleteDoctor(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {

        User user = userRepository.findByUsername(roleRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Role userRole = roleRepository.findByName("ROLE_" + roleRequest.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        if (!user.getRoles().contains(userRole)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, roleRequest.getRole() + " User is not a " + roleRequest.getRole(), null));
        }

        // Remove the role from the user
        user.getRoles().remove(userRole);
        userRepository.save(user);

        // Return success response
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, roleRequest.getRole() + " role removed successfully!", null));
    }


    @GetMapping("/check")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDetails(@RequestParam String username, HttpServletRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        UserDTO userDTO = new UserDTO(user.getFirst_name() + " " + user.getLast_name(), user.getNic(),
                user.getUsername(), user.getEmail(), user.getRoles().toString());

        return ResponseEntity.ok(new ApiResponse<>(true, "User details are available.", userDTO));
    }

    @GetMapping("/byrole")
    public ResponseEntity<ApiResponse<Object>> getUsersByRole(@RequestParam String role, HttpServletRequest request) {
        Role userRole = roleRepository.findByName("ROLE_" + role.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        List<User> users = userRepository.findByRoles(Set.of(userRole));
        System.out.println(users);

        return ResponseEntity.ok(new ApiResponse<>(true, "Users with role " + role + " are available.", users));
    }

}