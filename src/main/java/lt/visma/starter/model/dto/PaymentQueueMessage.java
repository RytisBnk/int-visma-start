package lt.visma.starter.model.dto;

import lt.visma.starter.model.PaymentRequest;

import java.io.Serializable;
import java.util.Map;

public class PaymentQueueMessage implements Serializable {
    private String bankCode;
    private Map<String, String> authParams;
    private PaymentRequest paymentRequest;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Map<String, String> getAuthParams() {
        return authParams;
    }

    public void setAuthParams(Map<String, String> authParams) {
        this.authParams = authParams;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }
}
