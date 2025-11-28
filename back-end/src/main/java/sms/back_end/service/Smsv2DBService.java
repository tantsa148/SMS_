package sms.back_end.service;

import org.springframework.stereotype.Service;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.NumeroDestinataire;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.entity.SmsMessage;
import sms.back_end.repository.NumeroDestinataireRepository;
import sms.back_end.repository.NumeroExpediteurRepository;
import sms.back_end.repository.SmsMessageRepository;
@Service
public class Smsv2DBService {

    private final NumeroExpediteurRepository expediteurRepository;
    private final NumeroDestinataireRepository destinataireRepository;
    private final SmsMessageRepository messageRepository;

    public Smsv2DBService(
            NumeroExpediteurRepository expediteurRepository,
            NumeroDestinataireRepository destinataireRepository,
            SmsMessageRepository messageRepository) {

        this.expediteurRepository = expediteurRepository;
        this.destinataireRepository = destinataireRepository;
        this.messageRepository = messageRepository;
    }

    // Méthode existante pour SMS
    public SmsResponse sendSms(Long destinataireId, Long messageId, Long expediteurId) {
        SmsResponse response = new SmsResponse();
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
            String fromNumber = expediteur.getValeur();

            NumeroDestinataire destinataire = destinataireRepository.findById(destinataireId)
                    .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
            String toNumber = destinataire.getValeurNumero();

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
                response.setError("Impossible de récupérer le statut : " + e.getMessage());
            }

        } catch (ApiException e) {
            response.setStatus("FAILED");
            response.setError("Erreur Twilio : " + e.getMessage());
        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError("Erreur interne : " + e.getMessage());
        }

        return response;
    }

    // 🔹 Nouvelle méthode pour WhatsApp
    public SmsResponse sendWhatsapp(Long destinataireId, Long messageId, Long expediteurId) {
        SmsResponse response = new SmsResponse();
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
            String fromNumber = "whatsapp:" + expediteur.getValeur();

            NumeroDestinataire destinataire = destinataireRepository.findById(destinataireId)
                    .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
            String toNumber = "whatsapp:" + destinataire.getValeurNumero();

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
                response.setError("Impossible de récupérer le statut : " + e.getMessage());
            }

        } catch (ApiException e) {
            response.setStatus("FAILED");
            response.setError("Erreur Twilio : " + e.getMessage());
        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError("Erreur interne : " + e.getMessage());
        }

        return response;
    }
}
