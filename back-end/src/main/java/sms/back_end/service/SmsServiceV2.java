package sms.back_end.service;

import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.repository.NumeroExpediteurRepository;
@Service
public class SmsServiceV2 {

    private final NumeroExpediteurRepository expediteurRepository;
    private final NumeroDestinataireService destinataireService;

    public SmsServiceV2(
            NumeroExpediteurRepository expediteurRepository,
            NumeroDestinataireService destinataireService) 
    {
        this.expediteurRepository = expediteurRepository;
        this.destinataireService = destinataireService;
    }

    // Méthode existante pour SMS
    public SmsResponse sendSmsUsingId(Long destinataireId, String messageBody, Long expediteurId) {
        SmsResponse response = new SmsResponse();
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
            String fromNumber = expediteur.getValeur();
            String destinataire = destinataireService.getValeurNumeroById(destinataireId);

            response.setExpediteur(fromNumber);
            response.setDestinataire(destinataire);
            response.setMessage(messageBody);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(destinataire),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            response.setTwilioResponse(twilioMessage.getSid());
            response.setStatus(Message.fetcher(twilioMessage.getSid()).fetch().getStatus().toString());

        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError(e.getMessage());
        }
        return response;
    }

    // 🔹 Nouvelle méthode pour WhatsApp
    public SmsResponse sendWhatsappUsingId(Long destinataireId, String messageBody, Long expediteurId) {
        SmsResponse response = new SmsResponse();
        try {
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

            // Préfixe whatsapp: obligatoire pour Twilio
            String fromNumber = "whatsapp:" + expediteur.getValeur();
            String destinataire = "whatsapp:" + destinataireService.getValeurNumeroById(destinataireId);

            response.setExpediteur(fromNumber);
            response.setDestinataire(destinataire);
            response.setMessage(messageBody);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(destinataire),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            response.setTwilioResponse(twilioMessage.getSid());
            response.setStatus(Message.fetcher(twilioMessage.getSid()).fetch().getStatus().toString());

        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setError(e.getMessage());
        }
        return response;
    }
}
