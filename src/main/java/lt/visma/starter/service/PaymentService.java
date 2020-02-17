package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.Transaction;

public interface PaymentService {
    PaymentResponse makePayment(String accessToken, PaymentRequest paymentRequest) throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
