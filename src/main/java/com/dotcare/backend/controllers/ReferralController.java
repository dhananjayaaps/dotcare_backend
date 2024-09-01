package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.ReferralDTO;
import com.dotcare.backend.dto.ReferralResponseDTO;
import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.entity.Referral;
import com.dotcare.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/referrals")
public class ReferralController {

    @Autowired
    private ReferralService referralService;

    @PostMapping
    public ResponseEntity<Referral> createReferral(@RequestBody ReferralDTO referralDTO) {
        Referral referral = referralService.createReferral(referralDTO);
        return ResponseEntity.ok(referral);
    }

    @GetMapping
    public ResponseEntity<List<Referral>> getAllReferrals() {
        return ResponseEntity.ok(referralService.getAllReferrals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Referral> getReferralById(@PathVariable Long id) {
        Referral referral = referralService.getReferralById(id);
        return ResponseEntity.ok(referral);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Referral> updateReferral(@PathVariable Long id, @RequestBody ReferralDTO referralDTO) {
        Referral updatedReferral = referralService.updateReferral(id, referralDTO);
        return ResponseEntity.ok(updatedReferral);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferral(@PathVariable Long id) {
        referralService.deleteReferral(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myAppointments")
    public List<ReferralResponseDTO> getReferralsByDoctorId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String doctorId = auth.getName();

        List<Referral> referrals = referralService.getReferralsByDoctorId(doctorId);

        return referrals.stream().map(referral -> {
            Mother mother = referral.getMother();
            return new ReferralResponseDTO(
                    mother.getName(),
                    mother.getNic(),
                    List.of(referral)
            );
        }).collect(Collectors.toList());
    }
}
