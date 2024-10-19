package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.GetRefferelWithRF;
import com.dotcare.backend.dto.ViewMotherDTO;
import com.dotcare.backend.entity.Clinic;
import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.repository.MotherRepository;
import com.dotcare.backend.service.MotherService;
import com.dotcare.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    @Autowired
    private MotherRepository motherRepository;

    @GetMapping("/{nic}")
    public ResponseEntity<Mother> getMotherDetails(@PathVariable String nic) {
        Optional<Mother> mother = motherService.getMotherByNic(nic);
        return mother.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/details/{nic}")
    public ResponseEntity<Mother> getMotherDetailsView(@PathVariable String nic) {
        Optional<Mother> mother = motherService.getMotherLatestByNic(nic);
        return mother.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/moh")
    public List<ViewMotherDTO> getMothersForMoh() {

        Clinic clinic = referralService.getMohArea();

        List<Mother> mothers = motherService.getMothersByMohArea(clinic);

        //reverse list
        mothers = mothers.stream().sorted(Comparator.comparing(Mother::getName, String::compareToIgnoreCase).reversed()).toList();

        List<ViewMotherDTO> uniqueMothers = mothers.stream()
                .map(mother -> new ViewMotherDTO(
                        mother.getId(),
                        mother.getName(),
                        mother.getNic(),
                        mother.getReferrals()
                ))
                .collect(Collectors.toList());

        return uniqueMothers;
    }

    @GetMapping("/doctor")
    public List<ViewMotherDTO> getMothersForDoctor() {
        List<Mother> mothers = motherRepository.findAll();

        if (mothers.isEmpty()) {
            return Collections.emptyList();
        }

//        sort by deaccending of mother id
        mothers = mothers.stream().sorted(Comparator.comparing(Mother::getId).reversed()).toList();

        List<ViewMotherDTO> uniqueMothers = mothers.stream()
                .collect(Collectors.toMap(
                        Mother::getNic,
                        mother -> mother,
                        (existing, replacement) -> existing
                ))
                .values().stream()
                .sorted(Comparator.comparing(Mother::getName, String::compareToIgnoreCase))
                .map(mother -> new ViewMotherDTO(
                        mother.getId(),
                        mother.getName(),
                        mother.getNic(),
                        mother.getReferrals()
                ))
                .collect(Collectors.toList());

        return uniqueMothers;
    }


    @GetMapping("/getRFandMomById")
    public GetRefferelWithRF getRiskFactorsandMotherById(@RequestParam String id) {
        return motherService.getRiskFactorsMotherById(id);
    }

}
