package lt.visma.starter.service;

import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.TransactionNotFoundException;
import lt.visma.starter.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface SavedTransactionsService {
    List<Transaction> getAllTransactions();
    Optional<Transaction> getTransactionById(long id) throws TransactionNotFoundException;
    Transaction saveTransaction(Transaction transaction) throws GenericException;
    boolean supportsBank(String bankCode);
}
