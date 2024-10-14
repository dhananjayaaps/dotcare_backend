package com.dotcare.backend.controllers;

import com.dotcare.backend.dto.ApiResponse;
import com.dotcare.backend.dto.ClinicDTO;
import com.dotcare.backend.dto.DoctorInfoDTO;
import com.dotcare.backend.entity.Clinic;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.service.ClinicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserDetailsService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Object>> addClinic(@RequestBody ClinicDTO clinicReq, HttpServletRequest request) {

        Clinic savedClinic = clinicService.saveClinic(clinicReq);
        return ResponseEntity.ok(new ApiResponse<>(true, "Clinic added successfully!", savedClinic));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> getClinics(HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Clinics fetched successfully!", clinicService.getClinics()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable Long id) {
        try {
            boolean isDeleted = clinicService.deleteClinicById(id);
            if (isDeleted) {
                return ResponseEntity.ok("Clinic deleted successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Clinic not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting clinic.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateClinic(@RequestBody ClinicDTO clinic, @PathVariable Long id,HttpServletRequest request) {
        clinic.setId(id);
        Clinic updatedClinic = clinicService.updateClinic(clinic);
        return ResponseEntity.ok(new ApiResponse<>(true, "Clinic updated successfully!", updatedClinic));
    }

    @GetMapping("/listMoh")
    public ResponseEntity<List<DoctorInfoDTO>> getDoctors() {
        List<DoctorInfoDTO> mohList = clinicService.getAllMOH();
        return ResponseEntity.ok(mohList);
    }
}










