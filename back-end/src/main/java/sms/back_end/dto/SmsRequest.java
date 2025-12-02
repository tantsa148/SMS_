package sms.back_end.dto;

public class SmsRequest {

    private Long expediteurId;
    private String destinataire;
    private String message;
    
    // getters et setters
    public Long getExpediteurId() { return expediteurId; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }

    public String getDestinataire() { return destinataire; }
    public void setDestinataire(String destinataire) { this.destinataire = destinataire; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
