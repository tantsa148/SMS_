package sms.back_end.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class DisponibleSurId implements Serializable {

    private Long idNumero;
    private Long idPlateforme;

    public DisponibleSurId() {
    }

    public DisponibleSurId(Long idNumero, Long idPlateforme) {
        this.idNumero = idNumero;
        this.idPlateforme = idPlateforme;
    }

    public Long getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(Long idNumero) {
        this.idNumero = idNumero;
    }

    public Long getIdPlateforme() {
        return idPlateforme;
    }

    public void setIdPlateforme(Long idPlateforme) {
        this.idPlateforme = idPlateforme;
    }

    // Obligatoire pour les clés composites
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisponibleSurId that = (DisponibleSurId) o;
        return Objects.equals(idNumero, that.idNumero) &&
               Objects.equals(idPlateforme, that.idPlateforme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumero, idPlateforme);
    }
}
