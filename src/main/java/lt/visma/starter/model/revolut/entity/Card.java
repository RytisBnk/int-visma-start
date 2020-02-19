package lt.visma.starter.model.revolut.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Card {
    @JsonAlias({"card_number"})
    @Id
    private String cardNumber;
    @JsonAlias({"first_name"})
    private String firstName;
    @JsonAlias({"last_name"})
    private String lastName;
    private String phone;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
