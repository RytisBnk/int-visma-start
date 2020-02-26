package lt.visma.starter.jms;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.model.dto.PaymentQueueMessage;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.factory.PaymentServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueueMessageReceiver {
    private PaymentServiceFactory paymentServiceFactory;

    public PaymentQueueMessageReceiver(PaymentServiceFactory paymentServiceFactory) {
        this.paymentServiceFactory = paymentServiceFactory;
    }

    private Logger LOGGER = LoggerFactory.getLogger("PaymentQueueMessageReceiverLogger");

    @JmsListener(destination = "${jms.paymentQueueDestination}")
    public void receivePaymentQueueMessage(@Header(JmsHeaders.CORRELATION_ID) String id, PaymentQueueMessage message)
            throws BankNotSupportedException, InvalidPaymentResponseException, GenericException, ApiException {
        LOGGER.info(String.format("Message received with id: %s", id));

        PaymentService paymentService = paymentServiceFactory.getPaymentService(message.getBankCode());
        paymentService.makePayment(message);
    }
}
