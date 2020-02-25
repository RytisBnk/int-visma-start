package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.Payment;

import java.util.List;
import java.util.Map;

public interface PaymentTransactionService {
    Payment submitPayment(Map<String, String> authParams, PaymentRequest paymentRequest, String bankCode)
            throws GenericException, ApiException, InvalidTransactionException, InvalidPaymentResponseException;
    Payment getPaymentById(String id);
    List<Payment> getPayments();
    boolean supportsBank(String bankCode);
}
