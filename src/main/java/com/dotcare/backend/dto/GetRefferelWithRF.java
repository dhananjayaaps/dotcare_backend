package com.dotcare.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetRefferelWithRF {

    //to send the referral with the seperated risk factors list
    private ReferralDTO referralDTO;
    private List<RiskFactorDetail> riskFactors;

    public GetRefferelWithRF(ReferralDTO referralDTO, List<RiskFactorDetail> riskFactors) {
        this.referralDTO = referralDTO;
        this.riskFactors = riskFactors;
    }
}