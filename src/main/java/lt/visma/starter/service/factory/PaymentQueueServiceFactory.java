package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.PaymentQueueService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentQueueServiceFactory {
    private List<PaymentQueueService> paymentQueueServices;

    public PaymentQueueServiceFactory(List<PaymentQueueService> paymentQueueServices) {
        this.paymentQueueServices = paymentQueueServices;
    }

    public PaymentQueueService getPaymentQueueService(String bankCode) throws BankNotSupportedException {
        return paymentQueueServices.stream()
                .filter(paymentQueueService -> paymentQueueService.supportsBank(bankCode))
                .findFirst()
                .orElseThrow(() -> new BankNotSupportedException("Bank: " + bankCode + " is not supported"));
    }
}
