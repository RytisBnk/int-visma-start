package lt.visma.starter.mapper.impl.revolut;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import lt.visma.starter.mapper.PaymentMapper;
import lt.visma.starter.model.revolut.entity.TransactionLeg;
import org.springframework.stereotype.Component;

@Component
public class RevolutPaymentMapper implements PaymentMapper {
    @Override
    public Payment mapToPersistentPaymentObject(Transaction transaction) throws InvalidTransactionException {
        if (! (transaction instanceof RevolutTransaction)) {
            throw new InvalidTransactionException();
        }
        RevolutTransaction revolutTransaction = (RevolutTransaction) transaction;

        Payment payment = new Payment();

        TransactionLeg leg = revolutTransaction.getLegs().get(0);
        payment.setBankName("Revolut");
        payment.setDebtorAccount(leg.getAccountId());
        payment.setCreditorAccount(leg.getCounterparty().getAccountId());
        payment.setAmount(leg.getAmount());
        payment.setCurrency(leg.getCurrency());
        payment.setReference(leg.getDescription());
        payment.setId(revolutTransaction.getId());

        return payment;
    }

    @Override
    public boolean supportsBankTransaction(Transaction transaction) {
        return transaction instanceof RevolutTransaction;
    }
}
