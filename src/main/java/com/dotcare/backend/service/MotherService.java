package com.dotcare.backend.service;

import com.dotcare.backend.dto.BotQuestonDTO;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    // A placeholder method to represent sending data to your AI model
    public String callFlaskApi(String query) {
        // Flask API URL
        String url = "http://127.0.0.1:5000/askAboutMother";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"query\": \"" + query + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        // Make the POST request
        String response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();

        return response;
    }

    public BotQuestonDTO getRiskFactorsMotherByIdAI(String motherNIC, String question) {

        Mother mother = motherRepository.findByNic(motherNIC);

        List<Referral> referrals = referralRepository.findAllByMother(mother);

        // Filter out null or incomplete referrals
        for (int i = referrals.size() - 1; i >= 0; i--) {
            if (referrals.get(i).getAntenatalOrPostnatal() == null) {
                referrals.remove(i);
            } else {
                break;
            }
        }

        // Get the latest referral (the most recent one)
        Referral latestReferral = referrals.get(referrals.size() - 1);

        List<Referral> referrals2 = referralRepository.findAllByMother(mother);

        // Prepare the list of risk factors with their dates
        List<RiskFactorDetail> riskFactorDetails = new ArrayList<>();
        for (Referral ref : referrals2) {
            Optional<User> doctor = userRepository.findByUsername(ref.getRefferedBy());
            String doctorName = doctor.map(User::getFirst_name).orElse("Unknown");
            for (String riskFactor : ref.getRiskFactors()) {
                // Create a new RiskFactorDetail object and add it to the list
                riskFactorDetails.add(new RiskFactorDetail(riskFactor, ref.getDate(), doctorName));
            }
        }

        // Prepare the ReferralDTO for the latest referral
        ReferralDTO referralDTO = new ReferralDTO(
                mother.getNic(),
                mother.getName(),
                latestReferral.getAntenatalOrPostnatal(),
                latestReferral.getDeliveryDate(),
                latestReferral.getExpectedDateOfDelivery(),
                latestReferral.getPog(),
                latestReferral.getParityGravidity(),
                latestReferral.getParityParity(),
                latestReferral.getParityChildren(),
                latestReferral.getRiskFactors(),
                latestReferral.getReasonForRequest(),
                latestReferral.getModesOfDelivery(),
                latestReferral.getBirthWeight(),
                latestReferral.getPostnatalDay(),
                latestReferral.getDoctorId(),
                latestReferral.getChannelDate().toString()
        );

        // Prepare the DTO to send to the AI model
        GetRefferelWithRF dto = new GetRefferelWithRF(referralDTO, riskFactorDetails);

        // Build the query to send to the AI model
        StringBuilder data = new StringBuilder();
        data.append("Dataset: ");

        data.append("Name: ").append(mother.getName()).append(" ");
        data.append("This is a Mother of: ").append(latestReferral.getAntenatalOrPostnatal()).append(" ");

        data.append("This is the final diagnosis of the mother: ");

        if (Objects.equals(latestReferral.getAntenatalOrPostnatal(), "Antenatal")) {
            if (latestReferral.getExpectedDateOfDelivery() != null) {
                data.append(" ExpectedDateOfDelivery: ").append(latestReferral.getExpectedDateOfDelivery().toString()).append("");
            }
            if (latestReferral.getPog() != null) {
                data.append(" Pog: ").append(latestReferral.getPog()).append("");
            }

        } else {
            if (latestReferral.getModesOfDelivery() != null) {
                data.append(" ModesOfDelivery: ").append(latestReferral.getModesOfDelivery()).append("");
            }
            if (latestReferral.getBirthWeight() != null) {
                data.append(" BirthWeight: ").append(latestReferral.getBirthWeight()).append("");
            }
            if (latestReferral.getPostnatalDay() != null) {
                data.append(" PostnatalDay: ").append(latestReferral.getPostnatalDay()).append("");
            }
        }

        if (latestReferral.getParityGravidity() != null) {
            data.append(" ParityGravidity: ").append(latestReferral.getParityGravidity()).append("");
        }
        if (latestReferral.getParityParity() != null) {
            data.append(" ParityParity: ").append(latestReferral.getParityParity()).append("");
        }
        if (latestReferral.getParityChildren() != null) {
            data.append(" ParityChildren: ").append(latestReferral.getParityChildren()).append("");
        }
        if (latestReferral.getRiskFactors() != null && !latestReferral.getRiskFactors().isEmpty()) {
            data.append(" RiskFactors: ").append(String.join(", ", latestReferral.getRiskFactors())).append("");
        }
        if (latestReferral.getReasonForRequest() != null) {
            data.append(" Final Reason For Request: ").append(latestReferral.getReasonForRequest()).append("");
        }

        data.append(" ChannelDate: ").append(latestReferral.getChannelDate()).append("");

        // Append all old risk factors with their dates and doctor names
        for (RiskFactorDetail riskFactorDetail : riskFactorDetails) {
            System.out.println(" RiskFactor: " + riskFactorDetail.getRiskFactor() + " , Date: " + riskFactorDetail.getChannelDate() + " , Doctor: " + riskFactorDetail.getDoctorName() + " ");
            data.append(" RiskFactor: ").append(riskFactorDetail.getRiskFactor())
                    .append(" , Date: ").append(riskFactorDetail.getChannelDate())
                    .append(" , Doctor: ").append(riskFactorDetail.getDoctorName()).append(" ");
        }

        // Add the user's question to the query
        String query = data.toString() + "" + " My question is: " + question + ".  This question is ask by the doctor and make a answer for him. If the question is not related about the mother say " + "I am sorry, I can't answer this question. Only make healthcare related answers." + " ";

        // Send the query to the AI model and get the answer
        BotQuestonDTO response = new BotQuestonDTO();
        response.setAnswer(callFlaskApi(query));
        System.out.println(" Response from AI model: " + response.getAnswer());
//        get the answer and josnify it
        return response;
    }

}
