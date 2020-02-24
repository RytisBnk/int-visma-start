package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.Transaction;

import java.util.Map;

public interface PaymentService {
    PaymentResponse makePayment(PaymentRequest paymentRequest, Map<String, String> params) throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
