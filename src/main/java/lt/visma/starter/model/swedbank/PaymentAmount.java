package lt.visma.starter.model.swedbank;

import java.io.Serializable;

public class PaymentAmount implements Serializable {
    private String amount;
    private String currency;

    public PaymentAmount() {
    }

    public PaymentAmount(String amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
