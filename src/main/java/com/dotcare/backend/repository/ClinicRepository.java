package com.dotcare.backend.repository;

import com.dotcare.backend.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Clinic findByName(String name);
}
