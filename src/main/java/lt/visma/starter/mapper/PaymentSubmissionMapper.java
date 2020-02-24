package lt.visma.starter.mapper;

import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.entity.PaymentSubmission;

public interface PaymentSubmissionMapper {
    PaymentSubmission mapToPaymentSubmission(PaymentResponse paymentResponse) throws InvalidPaymentResponseException;
}
