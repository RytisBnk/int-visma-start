package lt.visma.starter.jms;

import lt.visma.starter.model.dto.PaymentQueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueueMessageReceiver {


    private Logger LOGGER = LoggerFactory.getLogger("PaymentQueueMessageReceiverLogger");

    @JmsListener(destination = "${jms.paymentProcessingDestination}")
    public void receivePaymentQueueMessage(@Header(JmsHeaders.CORRELATION_ID) String id, PaymentQueueMessage message) {
        LOGGER.info(String.format("Message received with id: %s", id));
    }
}
