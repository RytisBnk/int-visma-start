package lt.visma.starter.mapper.impl.revolut;

import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.mapper.PaymentSubmissionMapper;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.entity.PaymentSubmission;
import lt.visma.starter.model.revolut.RevolutPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class RevolutPaymentSubmissionMapper implements PaymentSubmissionMapper {
    @Override
    public PaymentSubmission mapToPaymentSubmission(PaymentResponse paymentResponse) throws InvalidPaymentResponseException {
        if (! (paymentResponse instanceof RevolutPaymentResponse)) {
            throw new InvalidPaymentResponseException();
        }
        RevolutPaymentResponse revolutPaymentResponse = (RevolutPaymentResponse) paymentResponse;
        PaymentSubmission paymentSubmission = new PaymentSubmission();
        paymentSubmission.setBank("Revolut");
        paymentSubmission.setPaymentId(revolutPaymentResponse.getId());
        paymentSubmission.setStatus(revolutPaymentResponse.getState().getText());

        return paymentSubmission;
    }
}
