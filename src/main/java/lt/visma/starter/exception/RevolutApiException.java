package lt.visma.starter.exception;

import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.revolut.RevolutResponseError;

public class RevolutApiException extends ApiException {
    public RevolutApiException(ResponseError responseError) {
        super(responseError);
    }
}
