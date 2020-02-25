package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentServiceFactory {
    private final List<PaymentService> paymentServices;

    @Autowired
    public PaymentServiceFactory(List<PaymentService> paymentServices) {
        this.paymentServices = paymentServices;
    }

    public PaymentService getPaymentService(String bankCode) throws BankNotSupportedException {
        Optional<PaymentService> paymentServiceOptional = paymentServices.stream()
                .filter(paymentService -> paymentService.supportsBank(bankCode))
                .findFirst();
        if (!paymentServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return paymentServiceOptional.get();
    }
}
