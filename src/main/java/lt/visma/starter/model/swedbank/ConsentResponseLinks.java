package lt.visma.starter.model.swedbank;

public class ConsentResponseLinks {
    private ResponseLink scaRedirect;
    private ResponseLink scaStatus;
    private ResponseLink selectAuthenticationMethod;
    private ResponseLink startAuthorisation;
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

    public ResponseLink getSelectAuthenticationMethod() {
        return selectAuthenticationMethod;
    }

    public void setSelectAuthenticationMethod(ResponseLink selectAuthenticationMethod) {
        this.selectAuthenticationMethod = selectAuthenticationMethod;
    }

    public ResponseLink getStartAuthorisation() {
        return startAuthorisation;
    }

    public void setStartAuthorisation(ResponseLink startAuthorisation) {
        this.startAuthorisation = startAuthorisation;
    }

    public ResponseLink getStatus() {
        return status;
    }

    public void setStatus(ResponseLink status) {
        this.status = status;
    }
}
