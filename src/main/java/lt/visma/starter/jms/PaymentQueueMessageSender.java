package lt.visma.starter.jms;

import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.dto.PaymentQueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.Map;

@Component
public class PaymentQueueMessageSender {
    private final JmsTemplate jmsTemplate;
    private final Destination paymentQueueDestination;
    private final Logger LOGGER = LoggerFactory.getLogger("PaymentQueueMessageSender");

    public PaymentQueueMessageSender(JmsTemplate jmsTemplate, Destination paymentQueueDestination) {
        this.jmsTemplate = jmsTemplate;
        this.paymentQueueDestination = paymentQueueDestination;
    }

    public void sendPaymentQueueMessage(String messageId,
                                        String bankCode,
                                        Map<String, String> authParams,
                                        PaymentRequest paymentRequest) {
        PaymentQueueMessage messageBody = getMessageBody(bankCode, authParams, paymentRequest);
        jmsTemplate.convertAndSend(paymentQueueDestination, messageBody, message -> {
            message.setJMSCorrelationID(messageId);
            LOGGER.info(String.format("Sending message with id: %s", messageId));

            return message;
        });
    }

    private PaymentQueueMessage getMessageBody(String bankCode, Map<String, String> authParams, PaymentRequest paymentRequest) {
        PaymentQueueMessage paymentQueueMessage = new PaymentQueueMessage();
        paymentQueueMessage.setBankCode(bankCode);
        paymentQueueMessage.setAuthParams(authParams);
        paymentQueueMessage.setPaymentRequest(paymentRequest);

        return paymentQueueMessage;
    }
}
