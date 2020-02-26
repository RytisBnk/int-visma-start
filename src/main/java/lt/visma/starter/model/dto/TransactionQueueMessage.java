package lt.visma.starter.model.dto;

import lt.visma.starter.model.entity.PaymentSubmission;

import java.io.Serializable;
import java.util.Map;

public class TransactionQueueMessage implements Serializable {
    private String bankCode;
    private Map<String, String> authParams;
    private PaymentSubmission paymentSubmission;
    private String paymentQueueId;

    public String getPaymentQueueId() {
        return paymentQueueId;
    }

    public void setPaymentQueueId(String paymentQueueId) {
        this.paymentQueueId = paymentQueueId;
    }

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

    public PaymentSubmission getPaymentSubmission() {
        return paymentSubmission;
    }

    public void setPaymentSubmission(PaymentSubmission paymentSubmission) {
        this.paymentSubmission = paymentSubmission;
    }
}
