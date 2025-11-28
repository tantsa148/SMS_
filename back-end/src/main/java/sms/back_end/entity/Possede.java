package sms.back_end.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "possede")
@IdClass(PossedeId.class) // Clé primaire composite
public class Possede {

    @Id
    @Column(name = "id_utilisateur")
    private Long userId;

    @Id
    @Column(name = "id_numero")
    private Long numeroId;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    public Possede() {}

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getNumeroId() { return numeroId; }
    public void setNumeroId(Long numeroId) { this.numeroId = numeroId; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
