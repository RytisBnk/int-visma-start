package lt.visma.starter.exception;

import lt.visma.starter.model.revolut.ResponseError;

public class RevolutApiException extends RuntimeException {
    private final ResponseError responseError;

    public RevolutApiException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
