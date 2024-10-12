package com.dotcare.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private User moh;

    public Clinic() {
    }

    public Clinic(String name, User moh) {
        this.name = name;
        this.moh = moh;
    }

}
