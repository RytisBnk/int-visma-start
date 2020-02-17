package lt.visma.starter.exception;

import lt.visma.starter.model.ResponseError;

public class SwedbankApiException extends ApiException {
    private final ResponseError responseError;

    public SwedbankApiException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
