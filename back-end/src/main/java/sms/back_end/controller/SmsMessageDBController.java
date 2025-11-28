package sms.back_end.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.SmsDBRequest;
import sms.back_end.dto.SmsResponse;
import sms.back_end.service.SmsDBService;

@RestController
@RequestMapping("/api")
public class SmsMessageDBController {

    private final SmsDBService smsService;

    public SmsMessageDBController(SmsDBService smsService) {
        this.smsService = smsService;
    }

    // -----------------------------
    // POST /send : envoie un SMS dont le message est en base
    // -----------------------------
    @PostMapping("/sms-db/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody SmsDBRequest request) {
        SmsResponse response = smsService.sendSms(
                request.getExpediteurId(),
                request.getDestinataire(),
                request.getMessageId()
        );
        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // POST /send-whatsapp : envoie un message WhatsApp depuis la base
    // -----------------------------
    @PostMapping("/whatsapp-db/send")
    public ResponseEntity<SmsResponse> sendWhatsapp(@RequestBody SmsDBRequest request) {
        SmsResponse response = smsService.sendWhatsapp(
                request.getExpediteurId(),
                request.getDestinataire(),
                request.getMessageId()
        );
        return ResponseEntity.ok(response);
    }
}
