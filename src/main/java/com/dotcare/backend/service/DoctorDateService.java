package com.dotcare.backend.service;

import com.dotcare.backend.dto.DoctorDateDTO;
import com.dotcare.backend.entity.DoctorDate;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.DoctorDateRepository;
import com.dotcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorDateService {

    @Autowired
    private DoctorDateRepository doctorDateRepository;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository to fetch User entity.

    public DoctorDate createDoctorDate(DoctorDateDTO doctorDateDTO) {
        Optional<User> userOptional = userRepository.findByUsername(doctorDateDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            //find if there is a already entity with the same user
            Optional<DoctorDate> doctorDateOptional = doctorDateRepository.findByDoctorUser(user);
            if (doctorDateOptional.isPresent()) {
                DoctorDate doctorDate = doctorDateOptional.get();
                doctorDate.setDays(doctorDateDTO.getDays());
                return doctorDateRepository.save(doctorDate);
            } else{
                DoctorDate doctorDate = new DoctorDate(user, doctorDateDTO.getDays());
                return doctorDateRepository.save(doctorDate);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<DoctorDate> getAllDoctorDates() {
        return doctorDateRepository.findAll();
    }

    public DoctorDate getDoctorDateById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return doctorDateRepository.findByDoctorUser(user)
                .orElseThrow(() -> new RuntimeException("DoctorDate not found"));
    }

    public DoctorDate updateDoctorDate(Long id, DoctorDateDTO doctorDateDTO) {
        DoctorDate doctorDate = getDoctorDateById(id);

        Optional<User> userOptional = userRepository.findByUsername(doctorDateDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            doctorDate.setDoctorUser(user);
            doctorDate.setDays(doctorDateDTO.getDays());
            return doctorDateRepository.save(doctorDate);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteDoctorDate(Long id) {
        doctorDateRepository.deleteById(id);
    }
}
