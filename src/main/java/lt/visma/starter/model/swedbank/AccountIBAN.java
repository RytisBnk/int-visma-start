package lt.visma.starter.model.swedbank;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountIBAN {
    @Id
    private String iban;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
