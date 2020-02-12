package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PSUData {
    private String bankID;
    private String personalID;
    private String phoneNumber;

    public PSUData(String bankID, String personalID, String phoneNumber) {
        this.bankID = bankID;
        this.personalID = personalID;
        this.phoneNumber = phoneNumber;
    }

    public PSUData() {
    }

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
