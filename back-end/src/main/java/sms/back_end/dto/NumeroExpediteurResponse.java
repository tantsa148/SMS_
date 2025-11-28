package sms.back_end.dto;

import java.util.List;

public class NumeroExpediteurResponse {

    private Long id;
    private String valeur;
    private String dateCreation;
    private Long userId;
    private String userUsername;
    private List<String> plateformes;  // ✅ liste au lieu d'un seul nom
    private String message;

    public NumeroExpediteurResponse() {}

    public NumeroExpediteurResponse(Long id, String valeur, String dateCreation,
                                    Long userId, String userUsername,
                                    List<String> plateformes,
                                    String message) {
        this.id = id;
        this.valeur = valeur;
        this.dateCreation = dateCreation;
        this.userId = userId;
        this.userUsername = userUsername;
        this.plateformes = plateformes;
        this.message = message;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserUsername() { return userUsername; }
    public void setUserUsername(String userUsername) { this.userUsername = userUsername; }

    public List<String> getPlateformes() { return plateformes; }
    public void setPlateformes(List<String> plateformes) { this.plateformes = plateformes; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
