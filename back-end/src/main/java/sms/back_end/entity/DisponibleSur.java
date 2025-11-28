package sms.back_end.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "disponible_sur")
public class DisponibleSur {

    @EmbeddedId
    private DisponibleSurId id;

    public DisponibleSur() {}

    public DisponibleSur(DisponibleSurId id) {
        this.id = id;
    }

    public DisponibleSurId getId() {
        return id;
    }

    public void setId(DisponibleSurId id) {
        this.id = id;
    }
}
