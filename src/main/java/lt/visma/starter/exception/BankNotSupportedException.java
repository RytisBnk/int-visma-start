package lt.visma.starter.exception;

public class BankNotSupportedException extends Exception {
    public BankNotSupportedException(String message) {
        super(message);
    }

    public BankNotSupportedException() {
    }
}
