package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.PaymentTransactionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentTransactionServiceFactory {
    private final List<PaymentTransactionService> paymentTransactionServices;

    public PaymentTransactionServiceFactory(List<PaymentTransactionService> paymentTransactionServices) {
        this.paymentTransactionServices = paymentTransactionServices;
    }

    public PaymentTransactionService getPaymentTransactionService(String bankCode) throws BankNotSupportedException {
        return paymentTransactionServices.stream()
                .filter(paymentTransactionService -> paymentTransactionService.supportsBank(bankCode))
                .findFirst()
                .orElseThrow(() -> new BankNotSupportedException("Bank " + bankCode + " is not supported"));
    }
}
