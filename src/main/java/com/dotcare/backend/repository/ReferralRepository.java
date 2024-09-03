package com.dotcare.backend.repository;

import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    List<Referral> findAllById(Long id);

    List<Referral> findAllByDoctorId(String username);

    List<Referral> findAllByMother(Mother mother);
}
