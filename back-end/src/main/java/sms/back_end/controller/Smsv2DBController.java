package sms.back_end.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.SmsResponse;
import sms.back_end.dto.Smsv2DBRequest;
import sms.back_end.service.Smsv2DBService;

@RestController
@RequestMapping("/api")
public class Smsv2DBController {

    private final Smsv2DBService smsService;

    public Smsv2DBController(Smsv2DBService smsService) {
        this.smsService = smsService;
    }

    // -----------------------------
    // POST /send : envoie un SMS dont le message et le destinataire sont en base
    // -----------------------------
    @PostMapping("/sms_v2-DB/send")
    public ResponseEntity<SmsResponse> sendSms(@RequestBody Smsv2DBRequest request) {
        SmsResponse response = smsService.sendSms(
                request.getDestinataireId(),
                request.getMessageId(),
                request.getExpediteurId()
        );
        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // POST /send-whatsapp : envoie un message WhatsApp depuis la base
    // -----------------------------
    @PostMapping("/whatsapp_v2-DB/send")
    public ResponseEntity<SmsResponse> sendWhatsapp(@RequestBody Smsv2DBRequest request) {
        SmsResponse response = smsService.sendWhatsapp(
                request.getDestinataireId(),
                request.getMessageId(),
                request.getExpediteurId()
        );
        return ResponseEntity.ok(response);
    }
}
