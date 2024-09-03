package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RiskFactorDetail {
//    riskFactor, ref.getChannelDate(), doctorName
    private String riskFactor;
    private String channelDate;
    private String doctorName;

    public RiskFactorDetail(String riskFactor, LocalDate channelDate, String doctorName) {
        this.riskFactor = riskFactor;
        this.channelDate = String.valueOf(channelDate);
        this.doctorName = doctorName;
    }
}
