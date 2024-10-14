package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.GetRefferelWithRF;
import com.dotcare.backend.dto.ReferralDTO;
import com.dotcare.backend.dto.ViewMotherDTO;
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

    @PostMapping("/byMoh")
    public ResponseEntity<Referral> createReferralByMoh(@RequestBody ReferralDTO referralDTO) {
        referralDTO.setMohArea(String.valueOf(referralService.getMohArea().getId()));
        Referral referral = referralService.createReferral(referralDTO);
        return ResponseEntity.ok(referral);
    }

    @PostMapping("/byDoctor")
    public ResponseEntity<Referral> createReferralByDoctor(@RequestBody ReferralDTO referralDTO) {
        referralDTO.setMohArea(String.valueOf(referralService.getMohAreaByMohUsername(referralDTO.getDoctorId()).getId()));
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
    public List<ViewMotherDTO> getReferralsByDoctorId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String doctorId = auth.getName();

        List<Referral> referrals = referralService.getReferralsByDoctorId(doctorId);

        return referrals.stream().map(referral -> {
            Mother mother = referral.getMother();
            return new ViewMotherDTO(
                    referral.getId(),
                    mother.getName(),
                    mother.getNic(),
                    List.of(referral)
            );
        }).collect(Collectors.toList());
    }

    @GetMapping("/myMothers")
    public List<ViewMotherDTO> getMothersByDoctorId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String doctorId = auth.getName();

        List<Referral> referrals = referralService.getReferralsByDoctorId(doctorId);

        // Collect unique mothers by their NIC and sort by name
        List<ViewMotherDTO> uniqueMothers = referrals.stream()
                .collect(Collectors.toMap(
                        referral -> referral.getMother().getNic(), // Use NIC as the key to ensure uniqueness
                        referral -> referral, // Map referral to itself
                        (existing, replacement) -> existing // Keep the first referral if duplicates are found
                ))
                .values().stream()
                .sorted((r1, r2) -> r1.getMother().getName().compareToIgnoreCase(r2.getMother().getName())) // Sort by mother's name
                .map(referral -> {
                    Mother mother = referral.getMother();
                    return new ViewMotherDTO(
                            referral.getId(),
                            mother.getName(),
                            mother.getNic(),
                            List.of(referral) // Keep referral details as required
                    );
                })
                .collect(Collectors.toList());

        return uniqueMothers;
    }


    @GetMapping("/RiskFactorsbyNic")
    public List<String> getRiskFactorsByNic(@RequestParam String nic) {
        return referralService.getRiskFactorsByNic(nic);
    }

    @GetMapping("/getRFandMomById")
    public GetRefferelWithRF getRiskFactorsandMotherById(@RequestParam String id) {
         return referralService.getRiskFactorsMotherById(id);
    }

    @PostMapping("/addRiskFactor")
    public ResponseEntity<Referral> addFactorForMother(@RequestBody ReferralDTO referralDTO) {
        Referral referral = referralService.createReferral(referralDTO);
        return ResponseEntity.ok(referral);
    }
}
