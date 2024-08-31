package com.dotcare.backend.repository;

import com.dotcare.backend.entity.Role;
import com.dotcare.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNic(String nic);

    Optional<User> findByEmail(String email);

    List<User> findByRoles(Set<Role> roles);
}
