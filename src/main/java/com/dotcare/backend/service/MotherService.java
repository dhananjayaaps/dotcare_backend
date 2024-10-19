package com.dotcare.backend.service;

import com.dotcare.backend.dto.GetRefferelWithRF;
import com.dotcare.backend.dto.ReferralDTO;
import com.dotcare.backend.dto.RiskFactorDetail;
import com.dotcare.backend.entity.Clinic;
import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.entity.Referral;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.MotherRepository;
import com.dotcare.backend.repository.ReferralRepository;
import com.dotcare.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MotherService {

    final CustomUserDetailsService userDetailsService;
    final ReferralService referralService;
    final ReferralRepository referralRepository;
    final MotherRepository motherRepository;
    final UserRepository userRepository;


    public MotherService(CustomUserDetailsService userDetailsService, ReferralService referralService, ReferralRepository referralRepository, MotherRepository motherRepository, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.referralService = referralService;
        this.referralRepository = referralRepository;
        this.motherRepository = motherRepository;
        this.userRepository = userRepository;
    }

    public Optional<Mother> getMotherByNic(String nic) {
        return Optional.ofNullable(motherRepository.findByNic(nic));
    }

    public List<Mother> getMothersByMohArea(Clinic clinic) {
        return motherRepository.findByMohId(clinic.getId());
    }

    public GetRefferelWithRF getRiskFactorsMotherById(String id) {
//        Optional<Referral> referralOptional = referralRepository.findById(Long.valueOf(referralId));

        Mother mother = motherRepository.findByNic(id);

//        List<Referral> referral = mother.getReferrals();
        List<Referral> referrals = referralRepository.findAllByMother(mother);

        List<RiskFactorDetail> riskFactorDetails = new ArrayList<>();
        for (Referral ref : referrals) {
            Optional<User> doctor = userRepository.findByUsername(ref.getRefferedBy());
            String doctorName = doctor.get().getFirst_name();
            for (String riskFactor : ref.getRiskFactors()) {
                // Create a new RiskFactorDetail object and add it to the list
                riskFactorDetails.add(new RiskFactorDetail(riskFactor, ref.getDate(), doctorName));
            }
        }

        for (int i = referrals.size() - 1; i >= 0; i--) {
            if (referrals.get(i).getAntenatalOrPostnatal() == null) {
                referrals.remove(i);
            }else {
                break;
            }
        }
        Referral referral = referrals.get(referrals.size() - 1);

        GetRefferelWithRF dto = new GetRefferelWithRF(
                new ReferralDTO(
                        mother.getNic(), mother.getName(), referral.getAntenatalOrPostnatal(),
                        referral.getDeliveryDate(), referral.getExpectedDateOfDelivery(), referral.getPog(),
                        referral.getParityGravidity(), referral.getParityParity(), referral.getParityChildren(),
                        referral.getRiskFactors(), referral.getReasonForRequest(), referral.getModesOfDelivery(),
                        referral.getBirthWeight(), referral.getPostnatalDay(), referral.getDoctorId(),
                        referral.getChannelDate().toString()
                ),
                riskFactorDetails
        );
        return dto;
    }

    public Optional<Mother> getMotherLatestByNic(String nic) {
        Mother mother = motherRepository.findByNic(nic);
        List<Referral> referrals = mother.getReferrals();

        for (int i = referrals.size() - 1; i >= 0; i--) {
            if (referrals.get(i).getAntenatalOrPostnatal() == null) {
                referrals.remove(i);
            }else {
                break;
            }
        }
        List<Referral> newList = new ArrayList<>(List.of());
        newList.add(referrals.get(referrals.size() -1));

        mother.setReferrals(newList);

        return Optional.of(mother);

    }


}
