package com.dotcare.backend.service;

import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.repository.MotherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MotherService {

    @Autowired
    private MotherRepository motherRepository;

    public Optional<Mother> getMotherByNic(String nic) {
        return Optional.ofNullable(motherRepository.findByNic(nic));
    }
}
