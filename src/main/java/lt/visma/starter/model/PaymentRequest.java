package lt.visma.starter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.model.swedbank.SwedbankPaymentRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = false
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RevolutPaymentRequest.class, name = "Revolut"),
        @JsonSubTypes.Type(value = SwedbankPaymentRequest.class, name = "Swedbank")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PaymentRequest {
}
