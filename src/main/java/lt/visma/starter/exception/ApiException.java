package lt.visma.starter.exception;

import lt.visma.starter.model.ResponseError;

public class ApiException extends Exception {
    private ResponseError responseError;

    public ApiException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
