package lt.visma.starter.service.factory;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.service.PaymentMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentMapperServiceFactory {
    private List<PaymentMapperService> paymentMapperServices;

    @Autowired
    public PaymentMapperServiceFactory(List<PaymentMapperService> paymentMapperServices) {
        this.paymentMapperServices = paymentMapperServices;
    }

    public PaymentMapperService getPaymentMapperService(Transaction transaction) throws InvalidTransactionException {
        Optional<PaymentMapperService> paymentMapperServiceOptional = paymentMapperServices.stream()
                .filter(paymentMapperService -> paymentMapperService.supportsBankTransaction(transaction))
                .findFirst();
        if (! paymentMapperServiceOptional.isPresent()) {
            throw new InvalidTransactionException();
        }
        return paymentMapperServiceOptional.get();
    }
}
