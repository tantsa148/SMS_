package sms.back_end.dto;

import sms.back_end.entity.DisponibleSur;

public class DisponibleSurWithPlateformeName {

    private DisponibleSur disponibleSur;
    private String nomPlateforme;

    public DisponibleSurWithPlateformeName() {
    }

    public DisponibleSurWithPlateformeName(DisponibleSur disponibleSur, String nomPlateforme) {
        this.disponibleSur = disponibleSur;
        this.nomPlateforme = nomPlateforme;
    }

    public DisponibleSur getDisponibleSur() {
        return disponibleSur;
    }

    public void setDisponibleSur(DisponibleSur disponibleSur) {
        this.disponibleSur = disponibleSur;
    }

    public String getNomPlateforme() {
        return nomPlateforme;
    }

    public void setNomPlateforme(String nomPlateforme) {
        this.nomPlateforme = nomPlateforme;
    }
}
