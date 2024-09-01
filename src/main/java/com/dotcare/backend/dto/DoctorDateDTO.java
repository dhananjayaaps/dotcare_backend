package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class DoctorDateDTO {
    @NotNull
    private String username;

    @NotNull
    private List<Integer> days;

    public DoctorDateDTO(String username, List<Integer> days) {
        this.username = username;
        this.days = days;
    }
}
