package lt.visma.starter.jms;

import lt.visma.starter.model.dto.TransactionQueueMessage;
import lt.visma.starter.model.entity.PaymentSubmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.Map;

@Component
public class TransactionQueueMessageSender {
    private final JmsTemplate jmsTemplate;
    private final Destination transactionQueueDestination;

    private final Logger LOGGER = LoggerFactory.getLogger("TransactionQueueMessageSender");

    public TransactionQueueMessageSender(JmsTemplate jmsTemplate, Destination transactionQueueDestination) {
        this.jmsTemplate = jmsTemplate;
        this.transactionQueueDestination = transactionQueueDestination;
    }

    public void sendTransactionQueueMessage(String bankCode,
                                            Map<String, String> authParams,
                                            PaymentSubmission paymentSubmission,
                                            String paymentQueueId) {
        TransactionQueueMessage messageBody = getTransactionQueueMessage(bankCode, authParams, paymentSubmission, paymentQueueId);
        jmsTemplate.convertAndSend(transactionQueueDestination, messageBody, message -> {
            message.setJMSCorrelationID(paymentQueueId);
            LOGGER.info(String.format("Message sent with id: %s", paymentQueueId));

            return message;
        });
    }

    private TransactionQueueMessage getTransactionQueueMessage(String bankCode,
                                                               Map<String, String> authParams,
                                                               PaymentSubmission paymentSubmission,
                                                               String paymentQueueId) {
        TransactionQueueMessage message = new TransactionQueueMessage();
        message.setAuthParams(authParams);
        message.setBankCode(bankCode);
        message.setPaymentSubmission(paymentSubmission);
        message.setPaymentQueueId(paymentQueueId);

        return message;
    }
}
