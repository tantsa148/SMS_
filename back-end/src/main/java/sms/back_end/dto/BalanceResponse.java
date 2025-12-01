package sms.back_end.dto;

public class BalanceResponse {
    private String balance;
    private String currency;

    // Constructeurs (optionnel)
    public BalanceResponse() {}

    public BalanceResponse(String balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    // Getters / Setters
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
