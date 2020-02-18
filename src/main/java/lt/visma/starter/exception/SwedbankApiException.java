package lt.visma.starter.exception;

import lt.visma.starter.model.ResponseError;

public class SwedbankApiException extends ApiException {
    public SwedbankApiException(ResponseError responseError) {
        super(responseError);
    }
}
