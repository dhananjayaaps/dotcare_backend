package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class ReferralDTO {
    private String nic; // Related to Mother
    private String name; // Related to Mother

    private String antenatalOrPostnatal;
    private String deliveryDate;
    private String expectedDateOfDelivery;
    private Integer pog; // Period of Gestation
    private Boolean parityGravidity;
    private Boolean parityParity;
    private Boolean parityChildren;
    private List<String> riskFactors;
    private String reason_for_request;
    private String modes_of_delivery;
    private String birth_weight;
    private String postnatal_day;
    private String doctorId;
    private String channelDate;

    // Default constructor
    public ReferralDTO() {}

    // Parameterized constructor
    public ReferralDTO(String nic, String name, String antenatalOrPostnatal, String deliveryDate,
                       String expectedDateOfDelivery, Integer pog, Boolean parityGravidity,
                       Boolean parityParity, Boolean parityChildren, List<String> riskFactors,
                       String reason_for_request, String modes_of_delivery, String birth_weight,
                       String postnatal_day, String doctorId, String channelDate) {

        this.nic = nic;
        this.name = name;
        this.antenatalOrPostnatal = antenatalOrPostnatal;
        this.deliveryDate = deliveryDate;
        this.expectedDateOfDelivery = expectedDateOfDelivery;
        this.pog = pog;
        this.parityGravidity = parityGravidity;
        this.parityParity = parityParity;
        this.parityChildren = parityChildren;
        this.riskFactors = riskFactors;
        this.reason_for_request = reason_for_request;
        this.modes_of_delivery = modes_of_delivery;
        this.birth_weight = birth_weight;
        this.postnatal_day = postnatal_day;
        this.doctorId = doctorId;
        this.channelDate = channelDate;
    }
}
