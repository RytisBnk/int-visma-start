package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.visma.starter.model.PaymentRequest;

import java.io.Serializable;

public class RevolutPaymentRequest implements PaymentRequest, Serializable {
    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("account_id")
    private String accountId;
    private PaymentReceiver receiver;
    private double amount;
    private String currency;
    private String reference;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PaymentReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(PaymentReceiver receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
