package sms.back_end.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "view_log_destinataire") // nom de la vue
public class LogDestinataireVue {

    @Id
    private Long log_id;  // La PK doit être unique, ici on prend l'id de log

    private String twilio_response;
    private String status;
    private Integer twilio_error_code;
    private Long expediteur_id;
    private Long destinataire_id;
    private String destinataire_numero;
    private Long message_id;
    
    @Column(name = "platform")
    private String platform;   // <-- nouvelle colonne

    private LocalDateTime created_at;

    // ---------------- Getters et Setters ----------------
    public Long getLog_id() { return log_id; }
    public void setLog_id(Long log_id) { this.log_id = log_id; }

    public String getTwilio_response() { return twilio_response; }
    public void setTwilio_response(String twilio_response) { this.twilio_response = twilio_response; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTwilio_error_code() { return twilio_error_code; }
    public void setTwilio_error_code(Integer twilio_error_code) { this.twilio_error_code = twilio_error_code; }

    public Long getExpediteur_id() { return expediteur_id; }
    public void setExpediteur_id(Long expediteur_id) { this.expediteur_id = expediteur_id; }

    public Long getDestinataire_id() { return destinataire_id; }
    public void setDestinataire_id(Long destinataire_id) { this.destinataire_id = destinataire_id; }

    public String getDestinataire_numero() { return destinataire_numero; }
    public void setDestinataire_numero(String destinataire_numero) { this.destinataire_numero = destinataire_numero; }

    public Long getMessage_id() { return message_id; }
    public void setMessage_id(Long message_id) { this.message_id = message_id; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }
}
