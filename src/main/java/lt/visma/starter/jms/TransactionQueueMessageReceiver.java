package lt.visma.starter.jms;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.dto.TransactionQueueMessage;
import lt.visma.starter.service.TransactionQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class TransactionQueueMessageReceiver {
    private final TransactionQueueService transactionQueueService;

    public TransactionQueueMessageReceiver(TransactionQueueService transactionQueueService) {
        this.transactionQueueService = transactionQueueService;
    }

    private final Logger LOGGER = LoggerFactory.getLogger("TransactionQueueMessageReceiver");

    @JmsListener(destination = "${jms.transactionQueueDestination}")
    public void receiveTransactionQueueMessage(@Header(JmsHeaders.CORRELATION_ID) String id, TransactionQueueMessage message)
            throws BankNotSupportedException, GenericException, ApiException, InvalidTransactionException {
        LOGGER.info(String.format("TransactionQueueMessage received with id: %s", id));

        transactionQueueService.processTransactionQueueMessage(message);
    }
}

