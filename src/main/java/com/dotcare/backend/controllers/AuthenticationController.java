package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.*;
import com.dotcare.backend.entity.Role;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.entity.VerificationToken;
import com.dotcare.backend.repository.RoleRepository;
import com.dotcare.backend.repository.UserRepository;
import com.dotcare.backend.repository.VerificationTokenRepository;
import com.dotcare.backend.service.CustomUserDetailsService;
import com.dotcare.backend.service.EmailService;
import com.dotcare.backend.util.JwtHelper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> registerUser(@RequestBody SignupRequest signupRequest, HttpServletRequest request) throws MessagingException {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "Error: Username is already taken!", null));
        }
        if (userRepository.existsByEmail(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "Error: Email is already taken!", null));
        }
        if (userRepository.existsByNic(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(false, "Error: NIC is already taken!", null));
        }

        try {
            // Create new user's account
            User user = new User(
                    signupRequest.getUsername(),
                    passwordEncoder.encode(signupRequest.getPassword()),
                    signupRequest.getEmail(),
                    signupRequest.getFirst_name(),
                    signupRequest.getLast_name(),
                    signupRequest.getNic(),
                    signupRequest.getPhoneNumber(),
                    signupRequest.getMarketing_accept()
            );

            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(userRole);

            user.setRoles(roles);
            userRepository.save(user);

            // Generate a verification token
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUser(user);
            verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));  // 24 hours
            verificationTokenRepository.save(verificationToken);

            // Send verification email asynchronously
            String verificationLink = request.getRequestURL().toString() + "/verify?token=" + token;
            emailService.sendVerificationEmail(user.getEmail(), verificationLink);

            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully! Please check your email for verification.", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Get the user and their roles
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        List<String> roles = user.getRoles().stream()
                .map(Role::getName) // Assuming Role class has a getName() method
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(jwt, roles);

        return ResponseEntity.ok()
                .header("Set-Cookie", "jwtToken=" + jwt + "; HttpOnly; Path=/; Max-Age=86400; SameSite=None; Secure")
                .body(response);
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Invalid verification token");
        }

        User user = verificationToken.getUser();
        if (verificationToken.getExpiryDate().before(new Date())) {
            return ResponseEntity.badRequest().body("Verification token expired");
        }

        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);  // Delete the token after verification

        return ResponseEntity.ok("Email verified successfully! You can now log in.");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok()
                .header("Set-Cookie", "jwtToken=; HttpOnly; Path=/; Max-Age=0; SameSite=None; Secure")
                .body("Logged out successfully");
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Checked");
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserDetailsResponse> getUser() {
        UserDetails userDetails = userDetailsService.getCurrentUser();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(
                user.getUsername(),
                user.getFirst_name() + " " + user.getLast_name(),
                user.getEmail(), true
        );
        return ResponseEntity.ok(userDetailsResponse);
    }

}
