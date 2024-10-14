package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DoctorInfoDTO {
    private Long doctorId;
    private String doctorUsername;
    private String doctorName;
    private List<Integer> availableDays;

    public DoctorInfoDTO(Long doctorId, String doctorUsername, String doctorName, List<Integer> availableDays) {
        this.doctorId = doctorId;
        this.doctorUsername = doctorUsername;
        this.doctorName = doctorName;
        this.availableDays = availableDays;
    }

    public DoctorInfoDTO(Long doctorId, String doctorUsername, String doctorName) {
        this.doctorId = doctorId;
        this.doctorUsername = doctorUsername;
        this.doctorName = doctorName;
        this.availableDays = List.of(1, 2, 3, 4, 5);
    }
}
