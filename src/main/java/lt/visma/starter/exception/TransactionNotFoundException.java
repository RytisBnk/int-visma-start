package lt.visma.starter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Specified transaction does not exist")
public class TransactionNotFoundException extends Exception {
}
