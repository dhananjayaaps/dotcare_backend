package com.dotcare.backend.service;

import com.dotcare.backend.entity.User;
import com.dotcare.backend.entity.VerificationToken;
import com.dotcare.backend.repository.UserRepository;
import com.dotcare.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@EnableAsync(proxyTargetClass=true)
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Email not verified");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }

    public UserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String doctorId = auth.getName();
        return loadUserByUsername(doctorId);
    }

    @Async
    public void deleteToken(VerificationToken verificationToken) {
        try {
            // Wait for 2 seconds (2000 milliseconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Handle interruption
            Thread.currentThread().interrupt();
            CompletableFuture.failedFuture(e);
            return;
        }

        verificationTokenRepository.delete(verificationToken);
        CompletableFuture.completedFuture(null);
    }


}
