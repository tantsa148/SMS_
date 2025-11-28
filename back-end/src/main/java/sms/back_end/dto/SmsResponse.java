package sms.back_end.dto;

public class SmsResponse {

    private String expediteur;
    private String destinataire;
    private String message;
    private String twilioResponse;
    private String status;
    private String error;
    // Getters et setters
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
    
    public String getError() {return error;}
    public void setError(String error) {this.error = error;}
}
