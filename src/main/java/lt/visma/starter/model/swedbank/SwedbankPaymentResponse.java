package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonAlias;
import lt.visma.starter.model.PaymentResponse;

public class SwedbankPaymentResponse implements PaymentResponse {
    @JsonAlias({"_links"})
    private PaymentResponseLinks links;
    private String chosenScaMethod;
    private String paymentId;
    private String scaMethods;
    private String transactionStatus;

    public PaymentResponseLinks getLinks() {
        return links;
    }

    public void setLinks(PaymentResponseLinks links) {
        this.links = links;
    }

    public String getChosenScaMethod() {
        return chosenScaMethod;
    }

    public void setChosenScaMethod(String chosenScaMethod) {
        this.chosenScaMethod = chosenScaMethod;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getScaMethods() {
        return scaMethods;
    }

    public void setScaMethods(String scaMethods) {
        this.scaMethods = scaMethods;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
