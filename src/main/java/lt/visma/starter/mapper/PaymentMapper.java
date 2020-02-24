package lt.visma.starter.mapper;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.Transaction;

public interface PaymentMapper {
    Payment mapToPersistentPaymentObject(Transaction transaction) throws InvalidTransactionException;
    boolean supportsBankTransaction(Transaction transaction);
}
