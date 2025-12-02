package sms.back_end.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import sms.back_end.dto.NumeroDestinataireRequest;
import sms.back_end.dto.NumeroDestinataireResponse;
import sms.back_end.dto.SmsResponse;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.entity.SmsMessage;
import sms.back_end.entity.TwilioErrorCode;
import sms.back_end.repository.NumeroExpediteurRepository;

@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final NumeroExpediteurRepository expediteurRepository;
    private final NumeroDestinataireService numeroDestinataireService;
    private final SmsMessageService smsMessageService;
    private final SmsResponseLogService smsResponseLogService;
    private final TwilioErrorCodeService twilioErrorCodeService;


    public SmsService(
            NumeroExpediteurRepository expediteurRepository,
            NumeroDestinataireService numeroDestinataireService,
            SmsMessageService smsMessageService,
            SmsResponseLogService smsResponseLogService,
            TwilioErrorCodeService twilioErrorCodeService
    ) {
        this.expediteurRepository = expediteurRepository;
        this.numeroDestinataireService = numeroDestinataireService;
        this.smsMessageService = smsMessageService;
        this.smsResponseLogService = smsResponseLogService;
        this.twilioErrorCodeService = twilioErrorCodeService;
    }


    // -------------------- Envoi SMS --------------------
    public SmsResponse sendSms(String destinataire, String messageBody, Long expediteurId) {
        logger.info("⏩ Envoi SMS - Destinataire: {}, Expéditeur ID: {}", destinataire, expediteurId);

        SmsResponse response = new SmsResponse();

        try {
            // 1️⃣ Enregistrer / mettre à jour le numéro destinataire
            NumeroDestinataireRequest request = new NumeroDestinataireRequest();
            request.setValeur(destinataire);

            NumeroDestinataireResponse destinataireResponse = numeroDestinataireService.createNumero(request);
            Long savedDestinataireId = destinataireResponse.getId();
            logger.info("✅ Numéro destinataire enregistré avec ID: {}", savedDestinataireId);

            // 2️⃣ Trouver l'expéditeur
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
            String fromNumber = expediteur.getValeur();
            logger.info("✅ Expéditeur trouvé: ID={}, Numéro={}", expediteur.getId(), fromNumber);

            // 3️⃣ Enregistrer le message
            SmsMessage messageEntity = new SmsMessage(messageBody);
            SmsMessage savedMessage = smsMessageService.createMessage(messageEntity);
            Long messageId = savedMessage.getId();
            logger.info("✅ Message enregistré avec ID: {}", messageId);

            // 4️⃣ Remplir la réponse avec les IDs
            response.setExpediteur(fromNumber);
            response.setExpediteurId(expediteur.getId());
            response.setDestinataire(destinataire);
            response.setDestinataireId(savedDestinataireId);
            response.setMessage(messageBody);
            response.setMessageId(messageId);
            response.setStatus("PROCESSING");
            response.setPlatform("SMS"); // Ajouter juste après avoir rempli les autres champs

            // 5️⃣ Envoi SMS via Twilio
            Message twilioMessage = Message.creator(
                    new PhoneNumber(destinataire),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            String twilioSid = twilioMessage.getSid();
            response.setTwilioResponse(twilioSid);

            // 6️⃣ Récupération statut et code d'erreur
            Message fetched = Message.fetcher(twilioSid).fetch();
            response.setStatus(fetched.getStatus().toString());
            if (fetched.getErrorCode() != null) {
                response.setTwilioErrorCode(fetched.getErrorCode());
                response.setError(fetched.getErrorMessage());
            }

            logger.info("✅ SMS envoyé via Twilio - SID: {}, Status: {}, ErrorCode: {}",
                    twilioSid, response.getStatus(), response.getTwilioErrorCode());

        }catch (ApiException e) {
            String cleaned = cleanTwilioError(e.getMessage());

            response.setStatus("failed");
            response.setError(cleaned);
            response.setTwilioErrorCode(e.getCode());

            // 🔥 Enregistrement automatique en DB
            TwilioErrorCode errorEntity = new TwilioErrorCode(
                    e.getCode(),
                    cleaned,
                    e.getMoreInfo()
            );

            // Ajouter uniquement si pas déjà présent
            twilioErrorCodeService.getById(e.getCode())
                    .orElseGet(() -> twilioErrorCodeService.create(errorEntity));
        }

        catch (Exception e) {
            response.setStatus("failed");
            response.setError(cleanTwilioError(e.getMessage()));
        }

        // 7️⃣ Sauvegarde du log
        smsResponseLogService.saveResponse(response);
        logger.info("📤 Réponse SMS loguée avec messageId: {}", response.getMessageId());

        return response;
    }

    // -------------------- Envoi WhatsApp --------------------
    public SmsResponse sendWhatsapp(String destinataire, String messageBody, Long expediteurId) {
        logger.info("⏩ Envoi WhatsApp - Destinataire: {}, Expéditeur ID: {}", destinataire, expediteurId);

        SmsResponse response = new SmsResponse();

        try {
            // 1️⃣ Sauvegarde numéro
            NumeroDestinataireRequest request = new NumeroDestinataireRequest();
            request.setValeur(destinataire);
            NumeroDestinataireResponse destinataireResponse = numeroDestinataireService.createNumero(request);
            Long savedDestinataireId = destinataireResponse.getId();
            logger.info("✅ Numéro WhatsApp enregistré avec ID: {}", savedDestinataireId);

            // 2️⃣ Trouver l'expéditeur
            NumeroExpediteur expediteur = expediteurRepository.findById(expediteurId)
                    .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
            String fromNumber = "whatsapp:" + expediteur.getValeur();
            String toNumber = "whatsapp:" + destinataire;

            // 3️⃣ Enregistrer le message
            SmsMessage messageEntity = new SmsMessage(messageBody);
            SmsMessage savedMessage = smsMessageService.createMessage(messageEntity);
            Long messageId = savedMessage.getId();
            logger.info("✅ Message WhatsApp enregistré avec ID: {}", messageId);

            // 4️⃣ Remplir la réponse
            response.setExpediteur(fromNumber);
            response.setExpediteurId(expediteur.getId());
            response.setDestinataire(toNumber);
            response.setDestinataireId(savedDestinataireId);
            response.setMessage(messageBody);
            response.setMessageId(messageId);
            response.setStatus("PROCESSING");
            response.setPlatform("WhatsApp"); // Ajouter juste après avoir rempli les autres champs

            // 5️⃣ Envoi WhatsApp via Twilio
            Message twilioMessage = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            String twilioSid = twilioMessage.getSid();
            response.setTwilioResponse(twilioSid);

            // 6️⃣ Récupération statut et code d'erreur
            Message fetched = Message.fetcher(twilioSid).fetch();
            response.setStatus(fetched.getStatus().toString());
            if (fetched.getErrorCode() != null) {
                response.setTwilioErrorCode(fetched.getErrorCode());
                response.setError(fetched.getErrorMessage());
            }

            logger.info("✅ WhatsApp envoyé via Twilio - SID: {}, Status: {}, ErrorCode: {}",
                    twilioSid, response.getStatus(), response.getTwilioErrorCode());

        }catch (ApiException e) {
            String cleaned = cleanTwilioError(e.getMessage());

            response.setStatus("failed");
            response.setError(cleaned);
            response.setTwilioErrorCode(e.getCode());

            // 🔥 Enregistrement automatique en DB
            TwilioErrorCode errorEntity = new TwilioErrorCode(
                    e.getCode(),
                    cleaned,
                    e.getMoreInfo()
            );

            // Ajouter uniquement si pas déjà présent
            twilioErrorCodeService.getById(e.getCode())
                    .orElseGet(() -> twilioErrorCodeService.create(errorEntity));
        }

        catch (Exception e) {
            response.setStatus("failed");
            response.setError(cleanTwilioError(e.getMessage()));
        }

        // 7️⃣ Sauvegarde du log
        smsResponseLogService.saveResponse(response);
        logger.info("📤 Réponse WhatsApp loguée avec messageId: {}", response.getMessageId());

        return response;
    }
    private String cleanTwilioError(String message) {
    if (message == null) return null;
    return message.replaceAll("\\s?[0-9+]{5,}", ""); 
}

}
