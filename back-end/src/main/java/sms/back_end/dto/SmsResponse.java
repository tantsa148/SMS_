package sms.back_end.dto;

public class SmsResponse {

    private String expediteur;
    private String destinataire;
    private String message;
    private String twilioResponse;
    private String status;
    private String error;
    private Integer twilioErrorCode;
    private Long expediteurId;
    private Long destinataireId;
    private Long messageId;

    // Nouveau champ pour la plateforme
    private String platform;

    // Getters et setters existants
    public String getExpediteur() { return expediteur; }
    public void setExpediteur(String expediteur) { this.expediteur = expediteur; }

    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTwilioResponse() { return twilioResponse; }
    public void setTwilioResponse(String twilioResponse) { this.twilioResponse = twilioResponse; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }

    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public Integer getTwilioErrorCode() { return twilioErrorCode; }
    public void setTwilioErrorCode(Integer twilioErrorCode) { this.twilioErrorCode = twilioErrorCode; }

    // Getter et setter pour la plateforme
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
}
