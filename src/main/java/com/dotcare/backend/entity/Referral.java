package com.dotcare.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mother_id", nullable = false)
    @JsonBackReference // Breaks the recursion
    private Mother mother;

    @Column(nullable = false)
    private String antenatalOrPostnatal;

    private String deliveryDate;
    private String expectedDateOfDelivery;
    private Integer pog; // Period of Gestation
    private Boolean parityGravidity;
    private Boolean parityParity;
    private Boolean parityChildren;

    @ElementCollection
    private List<String> riskFactors;

    @Column(nullable = false)
    private String reasonForRequest;

    private String modesOfDelivery;
    private String birthWeight;
    private String postnatalDay;
    private String doctorId;
    private LocalDate channelDate;
    private String RefferedBy;
    private Boolean status;
    private LocalDate date;

    public Referral() {
    }

    public Referral(Mother mother, String antenatalOrPostnatal, String deliveryDate, String expectedDateOfDelivery,
                    Integer pog, Boolean parityGravidity, Boolean parityParity, Boolean parityChildren,
                    List<String> riskFactors, String reasonForRequest, String modesOfDelivery, String birthWeight,
                    String postnatalDay, String doctorId, String channelDate, String RefferedBy) {
        this.mother = mother;
        this.antenatalOrPostnatal = antenatalOrPostnatal;
        this.deliveryDate = deliveryDate;
        this.expectedDateOfDelivery = expectedDateOfDelivery;
        this.pog = pog;
        this.parityGravidity = parityGravidity;
        this.parityParity = parityParity;
        this.parityChildren = parityChildren;
        this.riskFactors = riskFactors;
        this.reasonForRequest = reasonForRequest;
        this.modesOfDelivery = modesOfDelivery;
        this.birthWeight = birthWeight;
        this.postnatalDay = postnatalDay;
        this.doctorId = doctorId;
        this.channelDate = channelDate != null ? LocalDate.parse(channelDate) : null;
        this.RefferedBy = RefferedBy;
        this.status = true;
        this.date = LocalDate.now();
    }
}
