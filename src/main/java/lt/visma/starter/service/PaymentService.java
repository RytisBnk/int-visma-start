package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.PaymentSubmission;

public interface PaymentService {
    PaymentSubmission makePayment(PaymentRequest paymentRequest, String accessToken)
            throws GenericException, ApiException, InvalidPaymentResponseException;
    boolean supportsBank(String bankCode);
}
