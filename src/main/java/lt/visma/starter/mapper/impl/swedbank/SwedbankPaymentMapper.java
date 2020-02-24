package lt.visma.starter.mapper.impl.swedbank;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.swedbank.entity.SwedbankPaymentTransaction;
import lt.visma.starter.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

@Component
public class SwedbankPaymentMapper implements PaymentMapper {
    @Override
    public Payment mapToPersistentPaymentObject(Transaction transaction) throws InvalidTransactionException {
        if (! (transaction instanceof SwedbankPaymentTransaction)) {
            throw new InvalidTransactionException();
        }
        SwedbankPaymentTransaction paymentTransaction = (SwedbankPaymentTransaction) transaction;
        return new Payment(
                "Swedbank",
                paymentTransaction.getId(),
                paymentTransaction.getDebtorAccount().getIban(),
                paymentTransaction.getCreditorAccount().getIban(),
                Double.parseDouble(paymentTransaction.getInstructedAmount().getAmount()),
                paymentTransaction.getInstructedAmount().getCurrency(),
                paymentTransaction.getRemittanceInformationStructured().getReference()
        );
    }

    @Override
    public boolean supportsBankTransaction(Transaction transaction) {
        return transaction instanceof SwedbankPaymentTransaction;
    }
}
