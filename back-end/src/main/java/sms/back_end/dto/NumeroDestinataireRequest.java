package sms.back_end.dto;

public class NumeroDestinataireRequest {

    private String valeur;       // Le numéro à créer
    private Long plateformeId;   // L'ID de la plateforme (optionnel)

    // Getters et setters
    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Long getPlateformeId() {
        return plateformeId;
    }

    public void setPlateformeId(Long plateformeId) {
        this.plateformeId = plateformeId;
    }
}
