package sms.back_end.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sms_response_log")
public class SmsResponseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "twilio_response")
    private String twilioResponse;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "twilio_error_code")
    private Integer twilioErrorCode;

    @Column(name = "expediteur_id")
    private Long expediteurId;

    @Column(name = "destinataire_id")
    private Long destinataireId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "platform") // Nouveau champ
    private String platform;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ---------------- Constructeurs ----------------
    public SmsResponseLog() { }

    public SmsResponseLog(String status, Integer twilioErrorCode, Long expediteurId, 
                          Long destinataireId, Long messageId, String platform) {
        this.status = status;
        this.twilioErrorCode = twilioErrorCode;
        this.expediteurId = expediteurId;
        this.destinataireId = destinataireId;
        this.messageId = messageId;
        this.platform = platform;
    }

    public SmsResponseLog(String twilioResponse, String status, Integer twilioErrorCode, 
                          Long expediteurId, Long destinataireId, Long messageId, String platform) {
        this.twilioResponse = twilioResponse;
        this.status = status;
        this.twilioErrorCode = twilioErrorCode;
        this.expediteurId = expediteurId;
        this.destinataireId = destinataireId;
        this.messageId = messageId;
        this.platform = platform;
    }

    // ---------------- Getters ----------------
    public Long getId() { return id; }
    public String getTwilioResponse() { return twilioResponse; }
    public String getStatus() { return status; }
    public Integer getTwilioErrorCode() { return twilioErrorCode; }
    public Long getExpediteurId() { return expediteurId; }
    public Long getDestinataireId() { return destinataireId; }
    public Long getMessageId() { return messageId; }
    public String getPlatform() { return platform; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ---------------- Setters ----------------
    public void setId(Long id) { this.id = id; }
    public void setTwilioResponse(String twilioResponse) { this.twilioResponse = twilioResponse; }
    public void setStatus(String status) { this.status = status; }
    public void setTwilioErrorCode(Integer twilioErrorCode) { this.twilioErrorCode = twilioErrorCode; }
    public void setExpediteurId(Long expediteurId) { this.expediteurId = expediteurId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
