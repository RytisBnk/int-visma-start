package lt.visma.starter.service;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.Transaction;

public interface PaymentMapperService {
    Payment mapToPersistentPaymentObject(Transaction transaction) throws InvalidTransactionException;
    boolean supportsBankTransaction(Transaction transaction);
}
