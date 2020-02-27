package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.dto.TransactionQueueMessage;

public interface TransactionQueueService {
    void processTransactionQueueMessage(TransactionQueueMessage transactionQueueMessage) throws BankNotSupportedException, GenericException, ApiException, InvalidTransactionException;
}
