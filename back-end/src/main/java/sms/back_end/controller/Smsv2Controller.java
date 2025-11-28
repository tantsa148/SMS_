package sms.back_end.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.SmsResponse;
import sms.back_end.dto.Smsv2Request;
import sms.back_end.service.SmsServiceV2;

@RestController
@RequestMapping("/api")
public class Smsv2Controller {

    private final SmsServiceV2 smsService;

    public Smsv2Controller(SmsServiceV2 smsService) {
        this.smsService = smsService;
    }

    // -----------------------------
    // 1️⃣ Envoi SMS via ID destinataire
    // -----------------------------
    @PostMapping("sms_v2/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody Smsv2Request request) {
        SmsResponse response = smsService.sendSmsUsingId(
                request.getDestinataireId(),
                request.getMessage(),
                request.getExpediteurId()
        );
        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // 2️⃣ Envoi WhatsApp via ID destinataire
    // -----------------------------
    @PostMapping("whatsapp_v2/send")
    public ResponseEntity<SmsResponse> sendWhatsapp(@RequestBody Smsv2Request request) {
        SmsResponse response = smsService.sendWhatsappUsingId(
                request.getDestinataireId(),
                request.getMessage(),
                request.getExpediteurId()
        );
        return ResponseEntity.ok(response);
    }
}
