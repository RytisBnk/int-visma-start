package lt.visma.starter.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RevolutPaymentRequest.class, name = "Revolut")
})
public abstract class PaymentRequest {
}
