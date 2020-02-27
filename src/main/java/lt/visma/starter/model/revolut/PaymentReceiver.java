package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PaymentReceiver implements Serializable {
    @JsonProperty("counterparty_id")
    private String counterpartyId;
    @JsonProperty("account_id")
    private String accountId;

    public String getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
