package com.dotcare.backend.service;

import com.dotcare.backend.dto.ClinicDTO;
import com.dotcare.backend.dto.DoctorInfoDTO;
import com.dotcare.backend.entity.Clinic;
import com.dotcare.backend.entity.DoctorDate;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.ClinicRepository;
import com.dotcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClinicService {
    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    public Clinic saveClinic(ClinicDTO clinicReq) {
        Clinic existingClinic = clinicRepository.findByName(clinicReq.getName());
        Clinic clinic;

        if (existingClinic != null) {
            clinic = existingClinic;
        } else {
            clinic = new Clinic();
        }
        clinic.setName(clinicReq.getName());
        Optional<User> userOptional = userRepository.findByUsername(clinicReq.getMohUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            clinic.setMoh(user);
        } else {
            clinic.setMoh(null);
        }

        return clinicRepository.save(clinic);
    }


    public Object getClinics() {
        List<Clinic> clinics = clinicRepository.findAll();

        List<Map<String, Object>> clinicResponse = clinics.stream()
                .map(clinic -> {
                    Map<String, Object> clinicData = new HashMap<>();
                    clinicData.put("id", clinic.getId());
                    clinicData.put("name", clinic.getName());

                    if (clinic.getMoh() != null) {
                        Map<String, Object> mohData = new HashMap<>();
                        mohData.put("name", clinic.getMoh().getFirst_name() + " " + clinic.getMoh().getLast_name());
                        mohData.put("username", clinic.getMoh().getUsername());
                        clinicData.put("moh", mohData);
                    } else {
                        clinicData.put("moh", null);
                    }

                    return clinicData;
                })
                .toList();

        return Map.of(
                "success", true,
                "message", "Clinics fetched successfully!",
                "data", clinicResponse
        );
    }


    public void deleteClinic(ClinicDTO req) {
        Optional<Clinic> clinic = clinicRepository.findById(req.getId());

        if (clinic.isPresent()) {
            clinicRepository.delete(clinic.get());
        } else {
            throw new NoSuchElementException("Clinic not found with id: " + req.getId());
        }
    }

    public boolean deleteClinicById(Long id) {
        Optional<Clinic> clinic = clinicRepository.findById(id);

        if (clinic.isPresent()) {
            clinicRepository.delete(clinic.get());
            return true;
        } else {
            return false;
        }
    }

    public Clinic updateClinic(ClinicDTO clinicDTO) {
        // Fetch the clinic by its ID
        Optional<Clinic> existingClinicOpt = clinicRepository.findById(clinicDTO.getId());

        if (existingClinicOpt.isPresent()) {
            Clinic clinicToUpdate = existingClinicOpt.get();

            // Update clinic name if it's present in the DTO
            if (clinicDTO.getName() != null && !clinicDTO.getName().isEmpty()) {
                clinicToUpdate.setName(clinicDTO.getName());
            }

            // Find the MOH user by username
            Optional<User> mohUserOpt = userRepository.findByUsername(clinicDTO.getMohUsername());

            if (mohUserOpt.isPresent()) {
                // If MOH user is found, set the MOH user
                clinicToUpdate.setMoh(mohUserOpt.get());
            } else {
                // If no MOH user is found, set MOH to null
                clinicToUpdate.setMoh(null);
            }

            // Save and return the updated clinic
            return clinicRepository.save(clinicToUpdate);

        } else {
            // Throw an exception if clinic is not found
            throw new NoSuchElementException("Clinic not found with id: " + clinicDTO.getId());
        }
    }

    public List<DoctorInfoDTO> getAllMOH() {

        List<Clinic> allMohAreas = clinicRepository.findAll();

        return allMohAreas.stream()
                .map(mohArea -> new DoctorInfoDTO(
                        mohArea.getMoh().getId(),
                        mohArea.getMoh().getUsername(),
                        mohArea.getMoh().getFirst_name()+" "+mohArea.getMoh().getLast_name()))
                .collect(Collectors.toList());
    }

}
