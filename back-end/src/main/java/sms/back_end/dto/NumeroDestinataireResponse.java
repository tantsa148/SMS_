package sms.back_end.dto;

import java.util.List;

public class NumeroDestinataireResponse {

    private Long id;
    private String valeur;
    private String dateCreation;
    private List<String> plateformes; // ✅ Liste au lieu d'une chaîne
    private String message; // message

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public List<String> getPlateformes() { return plateformes; }
    public void setPlateformes(List<String> plateformes) { this.plateformes = plateformes; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
