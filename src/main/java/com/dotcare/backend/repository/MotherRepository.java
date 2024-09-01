package com.dotcare.backend.repository;

import com.dotcare.backend.entity.Mother;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotherRepository extends JpaRepository<Mother, Long> {
    Mother findByNic(String nic);
}
