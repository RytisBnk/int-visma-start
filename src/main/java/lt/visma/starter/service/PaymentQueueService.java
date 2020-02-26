package lt.visma.starter.service;

import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.PaymentQueueEntry;

import java.util.Map;

public interface PaymentQueueService {
    PaymentQueueEntry submitPaymentToQueue(String bankCode, Map<String, String> authParams, PaymentRequest paymentRequest);
    PaymentQueueEntry getQueueEntryById(String id);
    boolean supportsBank(String bankCode);
}
