package sms.back_end.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.BalanceResponse;
import sms.back_end.service.TwilioService; // <-- IMPORT OBLIGATOIRE

@RestController
public class TwilioController {

    private final TwilioService twilioService;

    public TwilioController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @GetMapping("/api/twilio/balance")
    public BalanceResponse getBalance() {
        return twilioService.getBalance();
    }
}
