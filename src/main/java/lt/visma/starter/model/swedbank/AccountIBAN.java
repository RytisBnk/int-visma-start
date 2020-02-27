package lt.visma.starter.model.swedbank;

import java.io.Serializable;

public class AccountIBAN implements Serializable {
    private String iban;

    public AccountIBAN() {
    }

    public AccountIBAN(String iban) {
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
