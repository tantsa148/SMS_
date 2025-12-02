package sms.back_end.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "vue_historique_sms")
public class VueHistoriqueSms {

    @Id
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "utilisateur")
    private String utilisateur;

    @Column(name = "expediteur_numero")
    private String expediteurNumero;

    @Column(name = "destinataire_numero")
    private String destinataireNumero;

    @Column(name = "message_texte")
    private String messageTexte;

    @Column(name = "status")
    private String status;

    @Column(name = "twilio_error_code")
    private Integer twilioErrorCode;

    @Column(name = "twilio_error_message")
    private String twilioErrorMessage;

    @Column(name = "twilio_error_more_info")
    private String twilioErrorMoreInfo;

    @Column(name = "twilio_sid")
    private String twilioSid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "platform")
    private String platform; // Nouveau champ pour la plateforme

    // ---------------- Getters ----------------
    public Long getLogId() { return logId; }
    public String getUtilisateur() { return utilisateur; }
    public String getExpediteurNumero() { return expediteurNumero; }
    public String getDestinataireNumero() { return destinataireNumero; }
    public String getMessageTexte() { return messageTexte; }
    public String getStatus() { return status; }
    public Integer getTwilioErrorCode() { return twilioErrorCode; }
    public String getTwilioErrorMessage() { return twilioErrorMessage; }
    public String getTwilioErrorMoreInfo() { return twilioErrorMoreInfo; }
    public String getTwilioSid() { return twilioSid; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPlatform() { return platform; }

    // Pas besoin de setters pour une entité immuable
}
