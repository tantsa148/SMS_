package sms.back_end.dto;

import sms.back_end.entity.DisponiblePlateforme;

public class DisponiblePlateformeDTO {

    private DisponiblePlateforme disponiblePlateforme; // L'entité elle-même
    private String nomPlateforme;                     // Le nom de la plateforme

    public DisponiblePlateformeDTO() {
    }

    public DisponiblePlateformeDTO(DisponiblePlateforme disponiblePlateforme, String nomPlateforme) {
        this.disponiblePlateforme = disponiblePlateforme;
        this.nomPlateforme = nomPlateforme;
    }

    public DisponiblePlateforme getDisponiblePlateforme() {
        return disponiblePlateforme;
    }

    public void setDisponiblePlateforme(DisponiblePlateforme disponiblePlateforme) {
        this.disponiblePlateforme = disponiblePlateforme;
    }

    public String getNomPlateforme() {
        return nomPlateforme;
    }

    public void setNomPlateforme(String nomPlateforme) {
        this.nomPlateforme = nomPlateforme;
    }
}
