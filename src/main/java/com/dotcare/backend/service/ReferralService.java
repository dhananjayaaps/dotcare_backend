package com.dotcare.backend.service;

import com.dotcare.backend.dto.ReferralDTO;
import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.entity.Referral;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.MotherRepository;
import com.dotcare.backend.repository.ReferralRepository;
import com.dotcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReferralService {

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private MotherRepository motherRepository;

    @Autowired
    private UserRepository userRepository;

    final CustomUserDetailsService userDetailsService;

    public ReferralService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Referral createReferral(ReferralDTO referralDTO) {
        Mother mother = motherRepository.findByNic(referralDTO.getNic());
        if (mother == null) {
            mother = new Mother(referralDTO.getNic(), referralDTO.getName());
            mother = motherRepository.save(mother);
        }

        Referral referral = new Referral(
                mother,
                referralDTO.getAntenatalOrPostnatal(),
                referralDTO.getDeliveryDate(),
                referralDTO.getExpectedDateOfDelivery(),
                referralDTO.getPog(),
                referralDTO.getParityGravidity(),
                referralDTO.getParityParity(),
                referralDTO.getParityChildren(),
                referralDTO.getRiskFactors(),
                referralDTO.getReason_for_request(),
                referralDTO.getModes_of_delivery(),
                referralDTO.getBirth_weight(),
                referralDTO.getPostnatal_day(),
                referralDTO.getDoctorId(),
                referralDTO.getChannelDate()
        );


        return referralRepository.save(referral);
    }

    public List<Referral> getAllReferrals() {
        return referralRepository.findAll();
    }

    public Referral getReferralById(Long id) {
        return referralRepository.findById(id).orElseThrow(() -> new RuntimeException("Referral not found"));
    }

    public Referral updateReferral(Long id, ReferralDTO referralDTO) {
        Referral referral = getReferralById(id);

        Mother mother = motherRepository.findByNic(referralDTO.getNic());
        if (mother == null) {
            mother = new Mother(referralDTO.getNic(), referralDTO.getName());
            mother = motherRepository.save(mother);
        }

        referral.setMother(mother);
        referral.setAntenatalOrPostnatal(referralDTO.getAntenatalOrPostnatal());
        referral.setDeliveryDate(referralDTO.getDeliveryDate());
        referral.setExpectedDateOfDelivery(referralDTO.getExpectedDateOfDelivery());
        referral.setPog(referralDTO.getPog());
        referral.setParityGravidity(referralDTO.getParityGravidity());
        referral.setParityParity(referralDTO.getParityParity());
        referral.setParityChildren(referralDTO.getParityChildren());
        referral.setRiskFactors(referralDTO.getRiskFactors());
        referral.setReasonForRequest(referralDTO.getReason_for_request());
        referral.setModesOfDelivery(referralDTO.getModes_of_delivery());
        referral.setBirthWeight(referralDTO.getBirth_weight());
        referral.setPostnatalDay(referralDTO.getPostnatal_day());
        referral.setDoctorId(referralDTO.getDoctorId());
        referral.setChannelDate(referralDTO.getChannelDate());

        return referralRepository.save(referral);
    }

    public void deleteReferral(Long id) {
        referralRepository.deleteById(id);
    }

    public List<Referral> getReferralsByDoctor(String doctorUsername) {
        return referralRepository.findAllByDoctorId(doctorUsername);
    }

    public List<Referral> getReferralsByDoctorId(String doctorId) {
        return referralRepository.findAllByDoctorId(doctorId);
    }
}
