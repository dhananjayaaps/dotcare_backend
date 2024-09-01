package com.dotcare.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class DoctorDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctorUser;

    @ElementCollection
    @Column()
    private List<Integer> days;

    public DoctorDate(User doctorUser, List<Integer> days) {
        this.doctorUser = doctorUser;
        this.days = days;
    }

    public DoctorDate() {
    }
}
