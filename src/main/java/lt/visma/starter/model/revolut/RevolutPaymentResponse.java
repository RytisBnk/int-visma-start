package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;
import lt.visma.starter.model.PaymentResponse;

public class RevolutPaymentResponse extends PaymentResponse {
    private PaymentState state;
    @JsonAlias({"created_at"})
    private String createdAt;
    @JsonAlias({"completed_at"})
    private String completedAt;
    @JsonAlias({"reason_code"})
    private String reasonCode;

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
