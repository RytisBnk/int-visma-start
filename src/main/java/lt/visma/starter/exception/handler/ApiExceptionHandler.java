package lt.visma.starter.exception.handler;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.model.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ResponseError> handleException(ApiException exc) {
        return new ResponseEntity<>(exc.getResponseError(), HttpStatus.BAD_REQUEST);
    }
}
