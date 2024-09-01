package com.dotcare.backend.dto;

import com.dotcare.backend.entity.Referral;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReferralResponseDTO {
    private String motherName;
    private String motherNic;
    private List<Referral> referrals;
}
