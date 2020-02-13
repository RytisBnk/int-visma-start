package lt.visma.starter.exception;

import lt.visma.starter.model.swedbank.ResponseError;

public class SwedbankApiException extends RuntimeException {
    private ResponseError responseError;

    public SwedbankApiException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }

    public void setResponseError(ResponseError responseError) {
        this.responseError = responseError;
    }
}
