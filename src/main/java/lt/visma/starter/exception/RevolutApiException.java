package lt.visma.starter.exception;

import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.revolut.RevolutResponseError;

public class RevolutApiException extends ApiException {
    private final ResponseError responseError;

    public RevolutApiException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
