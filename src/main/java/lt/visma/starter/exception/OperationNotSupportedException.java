package lt.visma.starter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Operation not supported for selected bank")
public class OperationNotSupportedException extends Exception {
}
