package com.dotcare.backend.controllers;

import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.service.MotherService;
import com.dotcare.backend.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/mother")
public class MotherController {

    @Autowired
    private MotherService motherService;

    @GetMapping("/{nic}")
    public ResponseEntity<Mother> getMotherDetails(@PathVariable String nic) {
        Optional<Mother> mother = motherService.getMotherByNic(nic);
        return mother.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
