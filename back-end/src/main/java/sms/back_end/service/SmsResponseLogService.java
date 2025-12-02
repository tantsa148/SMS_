package sms.back_end.service;

import org.springframework.stereotype.Service;

import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.SmsResponseLog;
import sms.back_end.repository.SmsResponseLogRepository;

@Service
public class SmsResponseLogService {

    private final SmsResponseLogRepository smsResponseLogRepository;

    public SmsResponseLogService(SmsResponseLogRepository smsResponseLogRepository) {
        this.smsResponseLogRepository = smsResponseLogRepository;
    }

    public void saveResponse(SmsResponse response) {
        SmsResponseLog log = new SmsResponseLog();
        log.setTwilioResponse(response.getTwilioResponse());
        log.setStatus(response.getStatus());
        log.setTwilioErrorCode(response.getTwilioErrorCode());
        log.setExpediteurId(response.getExpediteurId());
        log.setDestinataireId(response.getDestinataireId());
        log.setMessageId(response.getMessageId());

        // ✅ Ajout du champ platform
        log.setPlatform(response.getPlatform());

        smsResponseLogRepository.save(log);
    }
}
