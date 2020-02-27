package lt.visma.starter.mapper;

import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.entity.PaymentProcessingError;

public interface PaymentProcessingErrorMapper {
    PaymentProcessingError mapToPaymentProcessingError(ResponseError responseError);
    boolean supportObject(ResponseError responseError);
}
