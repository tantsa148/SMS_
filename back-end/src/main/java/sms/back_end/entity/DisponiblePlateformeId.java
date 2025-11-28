package sms.back_end.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DisponiblePlateformeId implements Serializable {

    @Column(name = "id_numero")
    private Long idNumero;

    @Column(name = "id_plateforme")
    private Long idPlateforme;

    public DisponiblePlateformeId() {}

    public DisponiblePlateformeId(Long idNumero, Long idPlateforme) {
        this.idNumero = idNumero;
        this.idPlateforme = idPlateforme;
    }

    public Long getIdNumero() { return idNumero; }
    public void setIdNumero(Long idNumero) { this.idNumero = idNumero; }

    public Long getIdPlateforme() { return idPlateforme; }
    public void setIdPlateforme(Long idPlateforme) { this.idPlateforme = idPlateforme; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisponiblePlateformeId)) return false;
        DisponiblePlateformeId that = (DisponiblePlateformeId) o;
        return Objects.equals(idNumero, that.idNumero) &&
               Objects.equals(idPlateforme, that.idPlateforme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumero, idPlateforme);
    }
}
