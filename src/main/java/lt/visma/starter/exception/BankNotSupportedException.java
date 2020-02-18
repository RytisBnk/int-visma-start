package lt.visma.starter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BankNotSupportedException extends Exception {
    public BankNotSupportedException(String message) {
        super(message);
    }

    public BankNotSupportedException() {
    }
}
