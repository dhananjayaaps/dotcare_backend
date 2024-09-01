package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.DoctorDateDTO;
import com.dotcare.backend.dto.DoctorInfoDTO;
import com.dotcare.backend.entity.DoctorDate;
import com.dotcare.backend.service.DoctorDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctordates")
public class DoctorDateController {

    @Autowired
    private DoctorDateService doctorDateService;

    @PostMapping
    public ResponseEntity<DoctorDate> createDoctorDate(@RequestBody DoctorDateDTO doctorDateDTO) {
        DoctorDate doctorDate = doctorDateService.createDoctorDate(doctorDateDTO);
        return ResponseEntity.ok(doctorDate);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDate>> getAllDoctorDates() {
        return ResponseEntity.ok(doctorDateService.getAllDoctorDates());
    }

    @GetMapping("/byId")
    public ResponseEntity<DoctorDate> getDoctorDateById(@RequestParam Long id) {
        DoctorDate doctorDate = doctorDateService.getDoctorDateById(id);
        return ResponseEntity.ok(doctorDate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDate> updateDoctorDate(@PathVariable Long id, @RequestBody DoctorDateDTO doctorDateDTO) {
        DoctorDate updatedDoctorDate = doctorDateService.updateDoctorDate(id, doctorDateDTO);
        return ResponseEntity.ok(updatedDoctorDate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorDate(@PathVariable Long id) {
        doctorDateService.deleteDoctorDate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorInfoDTO>> getDoctors() {
        List<DoctorInfoDTO> doctors = doctorDateService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}
