package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.PaymentAPIService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentServiceFactory {
    private final List<PaymentAPIService> paymentAPIServices;

    public PaymentServiceFactory(List<PaymentAPIService> paymentAPIServices) {
        this.paymentAPIServices = paymentAPIServices;
    }

    public PaymentAPIService getPaymentService(String bankCode) throws BankNotSupportedException {
        Optional<PaymentAPIService> paymentServiceOptional = paymentAPIServices.stream()
                .filter(paymentAPIService -> paymentAPIService.supportsBank(bankCode))
                .findFirst();
        if (!paymentServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return paymentServiceOptional.get();
    }
}
