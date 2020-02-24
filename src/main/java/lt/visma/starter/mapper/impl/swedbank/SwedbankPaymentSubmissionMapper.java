package lt.visma.starter.mapper.impl.swedbank;

import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.mapper.PaymentSubmissionMapper;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.entity.PaymentSubmission;
import lt.visma.starter.model.swedbank.SwedbankPaymentResponse;

public class SwedbankPaymentSubmissionMapper implements PaymentSubmissionMapper {
    @Override
    public PaymentSubmission mapToPaymentSubmission(PaymentResponse paymentResponse) throws InvalidPaymentResponseException {
        if (! (paymentResponse instanceof SwedbankPaymentResponse)) {
            throw new InvalidPaymentResponseException();
        }
        SwedbankPaymentResponse swedbankPaymentResponse = (SwedbankPaymentResponse) paymentResponse;

        PaymentSubmission paymentSubmission = new PaymentSubmission();
        paymentSubmission.setBank("Swedbank");
        paymentSubmission.setPaymentId(swedbankPaymentResponse.getId());
        paymentSubmission.setStatus(swedbankPaymentResponse.getTransactionStatus());

        return paymentSubmission;
    }
}
