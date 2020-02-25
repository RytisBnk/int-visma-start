package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.OperationNotSupportedException;
import lt.visma.starter.model.Transaction;

import java.util.List;
import java.util.Map;

public interface BankingAPITransactionService {
    List<Transaction> getTransactions(String from, String to, Map<String, String> authParams)
            throws GenericException, ApiException, OperationNotSupportedException;
    Transaction getTransactionById(String transactionId, String bankCode, Map<String, String> authParams)
            throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
