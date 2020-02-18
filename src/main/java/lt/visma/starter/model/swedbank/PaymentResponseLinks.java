package lt.visma.starter.model.swedbank;

public class PaymentResponseLinks {
    private ResponseLink scaRedirect;
    private ResponseLink scaStatus;
    private ResponseLink self;
    private ResponseLink status;

    public ResponseLink getScaRedirect() {
        return scaRedirect;
    }

    public void setScaRedirect(ResponseLink scaRedirect) {
        this.scaRedirect = scaRedirect;
    }

    public ResponseLink getScaStatus() {
        return scaStatus;
    }

    public void setScaStatus(ResponseLink scaStatus) {
        this.scaStatus = scaStatus;
    }

    public ResponseLink getSelf() {
        return self;
    }

    public void setSelf(ResponseLink self) {
        this.self = self;
    }

    public ResponseLink getStatus() {
        return status;
    }

    public void setStatus(ResponseLink status) {
        this.status = status;
    }
}
