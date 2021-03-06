package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.model.dto.PaymentQueueMessage;

public interface PaymentAPIService {
    void makePayment(PaymentQueueMessage paymentQueueMessage, String queueEntryId)
            throws GenericException, ApiException, InvalidPaymentResponseException;
    boolean supportsBank(String bankCode);
}
