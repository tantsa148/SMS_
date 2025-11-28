package sms.back_end.service;

import org.springframework.stereotype.Service;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.entity.SmsMessage;
import sms.back_end.repository.NumeroExpediteurRepository;
import sms.back_end.repository.SmsMessageRepository;

@Service
public class SmsDBService {

    private final NumeroExpediteurRepository expediteurRepository;
    private final SmsMessageRepository messageRepository;

    public SmsDBService(
            NumeroExpediteurRepository expediteurRepository,
            SmsMessageRepository messageRepository) {

        this.expediteurRepository = expediteurRepository;
        this.messageRepository = messageRepository;
    }

    // Méthode existante pour SMS
    public SmsResponse sendSms(Long expediteurId, String destinataire, Long messageId) {
        SmsResponse response = new SmsResponse();
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
            String fromNumber = expediteur.getValeur();

            SmsMessage messageEntity = messageRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message non trouvé"));
            String messageBody = messageEntity.getTexte();

            response.setExpediteur(fromNumber);
            response.setDestinataire(destinataire);
            response.setMessage(messageBody);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(destinataire),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            response.setTwilioResponse(twilioMessage.getSid());

            try {
                String status = Message.fetcher(twilioMessage.getSid())
                        .fetch()
                        .getStatus()
                        .toString();
                response.setStatus(status);
            } catch (Exception e) {
                response.setStatus("UNKNOWN");
                response.setError("Impossible de récupérer le statut.");
            }

        } catch (ApiException e) {
            response.setStatus("FAILED");
            response.setError("Erreur Twilio : " + e.getMessage());
        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError("Erreur : " + e.getMessage());
        }

        return response;
    }

    // 🔹 Nouvelle méthode pour WhatsApp
    public SmsResponse sendWhatsapp(Long expediteurId, String destinataire, Long messageId) {
        SmsResponse response = new SmsResponse();
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

            // Préfixe whatsapp: obligatoire pour Twilio
            String fromNumber = "whatsapp:" + expediteur.getValeur();
            String toNumber = "whatsapp:" + destinataire;

            SmsMessage messageEntity = messageRepository.findById(messageId)
                    .orElseThrow(() -> new RuntimeException("Message non trouvé"));
            String messageBody = messageEntity.getTexte();

            response.setExpediteur(fromNumber);
            response.setDestinataire(toNumber);
            response.setMessage(messageBody);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            response.setTwilioResponse(twilioMessage.getSid());

            try {
                String status = Message.fetcher(twilioMessage.getSid())
                        .fetch()
                        .getStatus()
                        .toString();
                response.setStatus(status);
            } catch (Exception e) {
                response.setStatus("UNKNOWN");
                response.setError("Impossible de récupérer le statut.");
            }

        } catch (ApiException e) {
            response.setStatus("FAILED");
            response.setError("Erreur Twilio : " + e.getMessage());
        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError("Erreur : " + e.getMessage());
        }

        return response;
    }
}
