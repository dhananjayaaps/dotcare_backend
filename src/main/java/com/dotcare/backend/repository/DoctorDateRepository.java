package com.dotcare.backend.repository;

import com.dotcare.backend.entity.DoctorDate;
import com.dotcare.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorDateRepository extends JpaRepository<DoctorDate, Long> {

    Optional<DoctorDate> findByDoctorUser(User user);
}
