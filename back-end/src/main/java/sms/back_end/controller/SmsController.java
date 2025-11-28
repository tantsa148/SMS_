package sms.back_end.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.SmsRequest;
import sms.back_end.dto.SmsResponse;
import sms.back_end.service.SmsService;

@RestController
@RequestMapping("/api")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/sms/send")
    public SmsResponse sendSms(@RequestBody SmsRequest request) {
        return smsService.sendSms(
                request.getDestinataire(),
                request.getMessage(),
                request.getExpediteurId()
        );
    }
      @PostMapping("/whatsapp/send")
    public SmsResponse sendWhatsapp(@RequestBody SmsRequest request) {
        return smsService.sendWhatsapp(
                request.getDestinataire(),
                request.getMessage(),
                request.getExpediteurId()
        );
    }
}
