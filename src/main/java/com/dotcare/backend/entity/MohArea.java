package com.dotcare.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class MohArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MohId;
    @Getter
    private String mohName;
    private String mohCode;
}