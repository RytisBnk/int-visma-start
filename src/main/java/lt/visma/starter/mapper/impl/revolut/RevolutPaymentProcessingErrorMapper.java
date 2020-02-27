package lt.visma.starter.mapper.impl.revolut;

import lt.visma.starter.mapper.PaymentProcessingErrorMapper;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.entity.PaymentProcessingError;
import lt.visma.starter.model.revolut.RevolutApiError;
import org.springframework.stereotype.Component;

@Component
public class RevolutPaymentProcessingErrorMapper implements PaymentProcessingErrorMapper {
    @Override
    public PaymentProcessingError mapToPaymentProcessingError(ResponseError responseError) {
        if (! (responseError instanceof RevolutApiError)) {
            throw new IllegalArgumentException();
        }
        RevolutApiError revolutApiError = (RevolutApiError) responseError;

        PaymentProcessingError paymentProcessingError = new PaymentProcessingError();
        paymentProcessingError.setType(Integer.toString(revolutApiError.getCode()));
        paymentProcessingError.setDescription(revolutApiError.getMessage());

        return paymentProcessingError;
    }

    @Override
    public boolean supportObject(ResponseError responseError) {
        return responseError instanceof RevolutApiError;
    }
}
