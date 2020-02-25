package lt.visma.starter.mapper.impl.swedbank;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.entity.Payment;
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
        Payment payment = new Payment();
        payment.setBankName("Swedbank");
        payment.setId(paymentTransaction.getId());
        payment.setCreditorAccount(paymentTransaction.getCreditorAccount().getIban());
        payment.setDebtorAccount(paymentTransaction.getDebtorAccount().getIban());
        payment.setAmount(Double.parseDouble(paymentTransaction.getInstructedAmount().getAmount()));
        payment.setCurrency(paymentTransaction.getInstructedAmount().getCurrency());
        payment.setReference(paymentTransaction.getRemittanceInformationStructured().getReference());

        return payment;
    }

    @Override
    public boolean supportsBankTransaction(Transaction transaction) {
        return transaction instanceof SwedbankPaymentTransaction;
    }
}
