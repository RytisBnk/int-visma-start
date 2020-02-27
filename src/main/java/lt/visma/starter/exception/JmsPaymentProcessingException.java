package lt.visma.starter.exception;

import lt.visma.starter.model.entity.PaymentProcessingError;

public class JmsPaymentProcessingException extends Exception {
    private final String paymentQueueId;
    private final PaymentProcessingError processingError;

    public JmsPaymentProcessingException(String paymentQueueId, PaymentProcessingError processingError) {
        this.paymentQueueId = paymentQueueId;
        this.processingError = processingError;
    }

    public String getPaymentQueueId() {
        return paymentQueueId;
    }

    public PaymentProcessingError getProcessingError() {
        return processingError;
    }
}
