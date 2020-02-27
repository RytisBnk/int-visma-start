package lt.visma.starter.mapper.impl.swedbank;

import lt.visma.starter.mapper.PaymentProcessingErrorMapper;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.entity.PaymentProcessingError;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import org.springframework.stereotype.Component;

@Component
public class SwedbankPaymentProcessingErrorMapper implements PaymentProcessingErrorMapper {
    @Override
    public PaymentProcessingError mapToPaymentProcessingError(ResponseError responseError) {
        if (! (responseError instanceof SwedbankResponseError)) {
            throw new IllegalArgumentException();
        }
        SwedbankResponseError swedbankResponseError = (SwedbankResponseError) responseError;

        PaymentProcessingError paymentProcessingError = new PaymentProcessingError();
        paymentProcessingError.setType(swedbankResponseError.getTppMessages().get(0).getCategory());
        paymentProcessingError.setDescription(swedbankResponseError.getTppMessages().get(0).getText());

        return paymentProcessingError;
    }

    @Override
    public boolean supportObject(ResponseError responseError) {
        return responseError instanceof SwedbankResponseError;
    }
}
