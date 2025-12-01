package sms.back_end.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity; // <-- IMPORT OBLIGATOIRE
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import sms.back_end.dto.BalanceResponse;

@Service
public class TwilioService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public BalanceResponse getBalance() {
        String url = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Balance.json";

        // Auth Basic
        String auth = accountSid + ":" + authToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<BalanceResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                BalanceResponse.class
        );

        return response.getBody();
    }
}
