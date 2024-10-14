package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.ReferralResponseDTO;
import com.dotcare.backend.entity.Clinic;
import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.entity.Referral;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.service.CustomUserDetailsService;
import com.dotcare.backend.service.MotherService;
import com.dotcare.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mother")
public class MotherController {

    @Autowired
    private MotherService motherService;

    @Autowired
    private ReferralService referralService;

    @GetMapping("/{nic}")
    public ResponseEntity<Mother> getMotherDetails(@PathVariable String nic) {
        Optional<Mother> mother = motherService.getMotherByNic(nic);
        return mother.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/moh")
    public List<ReferralResponseDTO> getMothersByDoctorId() {

        Clinic clinic = referralService.getMohArea();

        List<Mother> mothers = motherService.getMothersByMohArea(clinic);

        List<ReferralResponseDTO> uniqueMothers = mothers.stream()
                .collect(Collectors.toMap(
                        Mother::getNic,
                        mother -> mother,
                        (existing, replacement) -> existing
                ))
                .values().stream()
                .sorted(Comparator.comparing(Mother::getName, String::compareToIgnoreCase))
                .map(mother -> {
                    return new ReferralResponseDTO(
                            mother.getId(), // Assuming there's an ID field in Mother
                            mother.getName(),
                            mother.getNic(),
                            mother.getReferrals() // Assuming Mother contains a list of referrals
                    );
                })
                .collect(Collectors.toList());

        return uniqueMothers;
    }

}
