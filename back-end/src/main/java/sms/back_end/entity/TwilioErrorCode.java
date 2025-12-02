package sms.back_end.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "twilio_error_codes")
public class TwilioErrorCode {

    @Id
    private Integer code;

    private String message;

    private String moreInfo;

    // Constructeurs
    public TwilioErrorCode() {}

    public TwilioErrorCode(Integer code, String message, String moreInfo) {
        this.code = code;
        this.message = message;
        this.moreInfo = moreInfo;
    }

    // Getters et Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}
