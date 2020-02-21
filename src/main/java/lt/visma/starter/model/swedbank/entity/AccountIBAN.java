package lt.visma.starter.model.swedbank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountIBAN {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private String iban;

    public AccountIBAN() {
    }

    public AccountIBAN(String iban) {
        this.iban = iban;
    }

    public String getIban() {
        return iban;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
