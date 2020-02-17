package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactions(String accessToken, String from, String to) throws GenericException, ApiException;
    Transaction getTransactionById(String accessToken, String transactionId) throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
