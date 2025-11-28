package sms.back_end.service;

import org.springframework.stereotype.Service;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.repository.NumeroExpediteurRepository;

@Service
public class SmsService {

    private final NumeroExpediteurRepository expediteurRepository;

    public SmsService(NumeroExpediteurRepository expediteurRepository) {
        this.expediteurRepository = expediteurRepository;
    }

    public SmsResponse sendSms(String destinataire, String messageBody, Long expediteurId) {

        SmsResponse response = new SmsResponse(); // JSON final à retourner

        try {
            // -----------------------------
            // 1️⃣ Chercher l'expéditeur
            // -----------------------------
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

            String fromNumber = expediteur.getValeur();

            // Remplir les infos déjà connues
            response.setExpediteur(fromNumber);
            response.setDestinataire(destinataire);
            response.setMessage(messageBody);

            // -----------------------------
            // 2️⃣ Envoi du SMS via Twilio
            // -----------------------------
            Message twilioMessage = Message.creator(
                    new PhoneNumber(destinataire),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            response.setTwilioResponse(twilioMessage.getSid());

            // -----------------------------
            // 3️⃣ Récupérer le status actuel
            // -----------------------------
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
            // Erreur Twilio
            response.setStatus("FAILED");
            response.setError("Erreur Twilio : " + e.getMessage());

        } catch (Exception e) {
            // Erreur interne ou expéditeur non trouvé
            response.setStatus("FAILED");
            response.setError("Erreur interne : " + e.getMessage());
        }

        return response; // retourne toujours un JSON, même en erreur
    }
    
    //--------
    //Whatsapp
    //--------
    public SmsResponse sendWhatsapp(String destinataire, String messageBody, Long expediteurId) {
        SmsResponse response = new SmsResponse(); 
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

            // Twilio WhatsApp nécessite le préfixe "whatsapp:"
            String fromNumber = "whatsapp:" + expediteur.getValeur();
            String toNumber = "whatsapp:" + destinataire;

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
