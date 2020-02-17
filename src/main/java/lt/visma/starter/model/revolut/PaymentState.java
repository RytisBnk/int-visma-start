package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentState {
    PENDING("pending"),
    COMPLETED("completed"),
    DECLINED("declined"),
    FAILED("failed"),
    REVERTED("reverted");

    private String text;

    PaymentState(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
