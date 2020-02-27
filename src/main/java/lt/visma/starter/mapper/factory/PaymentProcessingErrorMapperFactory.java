package lt.visma.starter.mapper.factory;

import lt.visma.starter.mapper.PaymentProcessingErrorMapper;
import lt.visma.starter.mapper.impl.DefaultPaymentProcessingErrorMapper;
import lt.visma.starter.model.ResponseError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentProcessingErrorMapperFactory {
    private final List<PaymentProcessingErrorMapper> paymentProcessingErrorMappers;
    private final DefaultPaymentProcessingErrorMapper defaultMapper = new DefaultPaymentProcessingErrorMapper();

    public PaymentProcessingErrorMapperFactory(List<PaymentProcessingErrorMapper> paymentProcessingErrorMappers) {
        this.paymentProcessingErrorMappers = paymentProcessingErrorMappers;
    }

    public PaymentProcessingErrorMapper getPaymentProcessingErrorMapper(ResponseError responseError) {
        return paymentProcessingErrorMappers.stream()
                .filter(paymentProcessingErrorMapper -> paymentProcessingErrorMapper.supportObject(responseError))
                .findFirst()
                .orElse(defaultMapper);
    }
}
