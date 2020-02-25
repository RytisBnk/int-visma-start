package lt.visma.starter.mapper.factory;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentMapperFactory {
    private List<PaymentMapper> paymentMappers;

    public PaymentMapperFactory(List<PaymentMapper> paymentMappers) {
        this.paymentMappers = paymentMappers;
    }

    public PaymentMapper getPaymentMapperService(Transaction transaction) throws InvalidTransactionException {
        Optional<PaymentMapper> paymentMapperServiceOptional = paymentMappers.stream()
                .filter(paymentMapper -> paymentMapper.supportsBankTransaction(transaction))
                .findFirst();
        if (! paymentMapperServiceOptional.isPresent()) {
            throw new InvalidTransactionException();
        }
        return paymentMapperServiceOptional.get();
    }
}
