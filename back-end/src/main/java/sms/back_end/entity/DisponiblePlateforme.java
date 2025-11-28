package sms.back_end.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "disponible_plateforme")
public class DisponiblePlateforme {

    @EmbeddedId
    private DisponiblePlateformeId id;

    public DisponiblePlateforme() {}

    public DisponiblePlateforme(DisponiblePlateformeId id) {
        this.id = id;
    }

    public DisponiblePlateformeId getId() { return id; }
    public void setId(DisponiblePlateformeId id) { this.id = id; }
}
