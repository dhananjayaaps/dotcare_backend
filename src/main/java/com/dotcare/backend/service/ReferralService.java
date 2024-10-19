package com.dotcare.backend.service;

import com.dotcare.backend.dto.GetRefferelWithRF;
import com.dotcare.backend.dto.ReferralDTO;
import com.dotcare.backend.dto.RiskFactorDetail;
import com.dotcare.backend.entity.Clinic;
import com.dotcare.backend.entity.Mother;
import com.dotcare.backend.entity.Referral;
import com.dotcare.backend.entity.User;
import com.dotcare.backend.repository.ClinicRepository;
import com.dotcare.backend.repository.MotherRepository;
import com.dotcare.backend.repository.ReferralRepository;
import com.dotcare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private ClinicRepository clinicRepository;

    final CustomUserDetailsService userDetailsService;

    public ReferralService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Referral createReferral(ReferralDTO referralDTO) {
        Mother mother = motherRepository.findByNic(referralDTO.getNic());
        if (mother == null) {
            mother = new Mother(referralDTO.getNic(), referralDTO.getName(), referralDTO.getMohArea(), referralDTO.getDob());
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
                referralDTO.getChannelDate(),
                userDetailsService.getCurrentUser().getUsername()
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
        referral.setChannelDate(LocalDate.parse(referralDTO.getChannelDate()));

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

    public List<String> getRiskFactorsByNic(String nic) {
        Mother mother = motherRepository.findByNic(nic);
        List<Referral> referrals = referralRepository.findAllByMother(mother);
        List<String> riskFactors = referrals.stream()
                .map(Referral::getRiskFactors)
                .flatMap(List::stream)
                .toList();
        return riskFactors;
    }

    //get referral by id and add all the risk factors to the list
    public List<String> getRiskFactorsByReferralId(Long id) {
        Referral referral = referralRepository.findById(id).orElseThrow(() -> new RuntimeException("Referral not found"));
        List<String> riskFactors = referral.getRiskFactors();
        return riskFactors;
    }

    public GetRefferelWithRF getRiskFactorsMotherById(String referralId) {

        Optional<Referral> referralOptional = referralRepository.findById(Long.valueOf(referralId));

        if (referralOptional.isEmpty()) {
            // Handle the case where the referral is not found
            throw new RuntimeException("Referral not found");
        }
        Referral referral = referralOptional.get();
        Mother mother = referral.getMother();
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

    public Clinic getMohArea() {
        UserDetails user = userDetailsService.getCurrentUser();
        User currentUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return clinicRepository.findByMoh_Id(currentUser.getId());
    }

    public Clinic getMohAreaByMohUsername(String doctorId) {
        User currentUser = userRepository.findByUsername(doctorId).orElseThrow(() -> new RuntimeException("User not found"));
        return clinicRepository.findByMoh_Id(currentUser.getId());
    }
}
