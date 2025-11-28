package sms.back_end.service;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.stereotype.Service;

import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.NumeroDestinataire;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.repository.NumeroDestinataireRepository;
import sms.back_end.repository.NumeroExpediteurRepository;

@Service
public class Smsv2Service {

    private final NumeroExpediteurRepository expediteurRepository;
    private final NumeroDestinataireRepository destinataireRepository;

    public Smsv2Service(
            NumeroExpediteurRepository expediteurRepository,
            NumeroDestinataireRepository destinataireRepository) {

        this.expediteurRepository = expediteurRepository;
        this.destinataireRepository = destinataireRepository;
    }

    public SmsResponse sendSms(Long destinataireId, String messageBody, Long expediteurId) {

        SmsResponse response = new SmsResponse();

        try {
            // -----------------------------
            // 1️⃣ Chercher l'expéditeur
            // -----------------------------
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

            String fromNumber = expediteur.getValeur();

            // -----------------------------
            // 2️⃣ Chercher le destinataire
            // -----------------------------
            NumeroDestinataire destinataire = destinataireRepository.findById(destinataireId)
                    .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

            String toNumber = destinataire.getValeurNumero();

            // Remplir les infos déjà connues
            response.setExpediteur(fromNumber);
            response.setDestinataire(toNumber);
            response.setMessage(messageBody);

            // -----------------------------
            // 3️⃣ Envoi du SMS
            // -----------------------------
            Message twilioMessage = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            response.setTwilioResponse(twilioMessage.getSid());

            // -----------------------------
            // 4️⃣ Récupération du statut
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
            response.setStatus("FAILED");
            response.setError("Erreur Twilio : " + e.getMessage());

        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError("Erreur interne : " + e.getMessage());
        }

        return response;
    }
}
