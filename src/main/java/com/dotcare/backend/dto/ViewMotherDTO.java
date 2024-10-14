package com.dotcare.backend.dto;

import com.dotcare.backend.entity.Referral;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViewMotherDTO {
    private Long id;
    private String motherName;
    private String motherNic;
    private Referral referral;

    public ViewMotherDTO(Long id, String motherName, String motherNic, List<Referral> referrals) {
        this.id = id;
        this.motherName = motherName;
        this.motherNic = motherNic;

        for (int i = referrals.size() - 1; i >= 0; i--) {
            if (referrals.get(i).getAntenatalOrPostnatal() == null) {
                referrals.remove(i);
            }else {
                break;
            }
        }

        this.referral = referrals.get(referrals.size() - 1);
    }
}
