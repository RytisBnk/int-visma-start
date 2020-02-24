package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import lt.visma.starter.service.PaymentMapperService;
import org.springframework.stereotype.Service;

@Service
public class RevolutPaymentMapperServiceImpl implements PaymentMapperService {
    @Override
    public Payment mapToPersistentPaymentObject(Transaction transaction) throws InvalidTransactionException {
        if (! (transaction instanceof RevolutTransaction)) {
            throw new InvalidTransactionException();
        }
        RevolutTransaction revolutTransaction = (RevolutTransaction) transaction;

        return new Payment(
                "Revolut",
                revolutTransaction.getId(),
                revolutTransaction.getLegs().get(0).getAccountId(),
                revolutTransaction.getLegs().get(0).getCounterparty().getAccountId(),
                revolutTransaction.getLegs().get(0).getAmount(),
                revolutTransaction.getLegs().get(0).getCurrency(),
                revolutTransaction.getLegs().get(0).getDescription()
        );
    }

    @Override
    public boolean supportsBankTransaction(Transaction transaction) {
        return transaction instanceof RevolutTransaction;
    }
}
