package lt.visma.starter.model.revolut;

public enum PaymentState {
    PENDING("pending"),
    COMPLETED("completed"),
    DECLINED("declined"),
    FAILED("failed");

    private String text;

    PaymentState(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
