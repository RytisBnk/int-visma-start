package lt.visma.starter.mapper.impl;

import lt.visma.starter.mapper.PaymentProcessingErrorMapper;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.entity.PaymentProcessingError;

public class DefaultPaymentProcessingErrorMapper implements PaymentProcessingErrorMapper {
    @Override
    public PaymentProcessingError mapToPaymentProcessingError(ResponseError responseError) {
        PaymentProcessingError paymentProcessingError = new PaymentProcessingError();
        paymentProcessingError.setType("Internal server error");
        paymentProcessingError.setDescription("No message available");

        return paymentProcessingError;
    }

    @Override
    public boolean supportObject(ResponseError responseError) {
        return true;
    }
}
