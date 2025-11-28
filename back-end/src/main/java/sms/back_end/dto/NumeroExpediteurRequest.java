package sms.back_end.dto;

public class NumeroExpediteurRequest {

    private String valeur;       // numéro d'expéditeur
    private Long idPlateforme;   // ID de la plateforme choisie

    public NumeroExpediteurRequest() {}

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Long getIdPlateforme() {
        return idPlateforme;
    }

    public void setIdPlateforme(Long idPlateforme) {
        this.idPlateforme = idPlateforme;
    }
}
