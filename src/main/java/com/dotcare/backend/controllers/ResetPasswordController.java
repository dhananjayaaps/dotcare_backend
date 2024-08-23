package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.ForgetPassword;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/reset-password")
public class ResetPasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/verify")
    public ResponseEntity<String> validateResetToken(@RequestParam("token") String token) {
        VerificationToken resetToken = verificationTokenRepository.findByToken(token);

        if (resetToken == null) {
            return ResponseEntity.badRequest().body("Invalid reset token");
        }

        if (resetToken.getExpiryDate().before(new Date())) {
            return ResponseEntity.badRequest().body("Reset token expired");
        }

        return ResponseEntity.ok("Reset token is valid.");
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestResetPassword(@RequestBody ForgetPassword forgetPassword, HttpServletRequest request) throws MessagingException {

//        System.out.println("Email: " + email);
        User user = userRepository.findByEmail(forgetPassword.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Generate a reset token
        String token = UUID.randomUUID().toString();
        VerificationToken resetToken = new VerificationToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));  // 24 hours

        //if there is a token for the user, delete it
        VerificationToken oldToken = verificationTokenRepository.findByUser(user);
        if (oldToken != null) {
            verificationTokenRepository.delete(oldToken);
        }

        verificationTokenRepository.save(resetToken);

        // Send reset password email
        String resetLink = request.getRequestURL().toString().replace("/request", "/verify") + "?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

        return ResponseEntity.ok("Password reset link sent to your email.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ForgetPassword forgetPassword) {
        VerificationToken resetToken = verificationTokenRepository.findByToken(forgetPassword.getToken());

        if (resetToken == null) {
            return ResponseEntity.badRequest().body("Invalid reset token");
        }

        if (resetToken.getExpiryDate().before(new Date())) {
            return ResponseEntity.badRequest().body("Reset token expired");
        }

        if (!forgetPassword.getPassword().equals(forgetPassword.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        if (forgetPassword.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters long");
        }

        if (forgetPassword.getPassword().length() > 20) {
            return ResponseEntity.badRequest().body("Password must be at most 20 characters long");
        }

        if (!forgetPassword.getPassword().matches(".*\\d.*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one digit");
        }
        User user = resetToken.getUser();

        if (passwordEncoder.encode(forgetPassword.getPassword()).equals((user.getPassword()))) {
            return ResponseEntity.badRequest().body("New password must be different from the current password");
        }

        // Reset password
        user.setPassword(passwordEncoder.encode(forgetPassword.getPassword()));
        userRepository.save(user);

        verificationTokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password reset successfully.");
    }
}
