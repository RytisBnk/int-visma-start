package lt.visma.starter.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RevolutPaymentRequest.class, name = "Revolut")
})
public interface PaymentRequest {
}
