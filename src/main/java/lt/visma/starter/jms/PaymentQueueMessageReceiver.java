package lt.visma.starter.jms;

import lt.visma.starter.exception.*;
import lt.visma.starter.mapper.PaymentProcessingErrorMapper;
import lt.visma.starter.mapper.factory.PaymentProcessingErrorMapperFactory;
import lt.visma.starter.model.dto.PaymentQueueMessage;
import lt.visma.starter.model.entity.PaymentProcessingError;
import lt.visma.starter.service.PaymentAPIService;
import lt.visma.starter.service.factory.PaymentServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueueMessageReceiver {
    private final PaymentServiceFactory paymentServiceFactory;
    private final PaymentProcessingErrorMapperFactory paymentProcessingErrorMapperFactory;

    public PaymentQueueMessageReceiver(PaymentServiceFactory paymentServiceFactory,
                                       PaymentProcessingErrorMapperFactory paymentProcessingErrorMapperFactory) {
        this.paymentServiceFactory = paymentServiceFactory;
        this.paymentProcessingErrorMapperFactory = paymentProcessingErrorMapperFactory;
    }

    private Logger LOGGER = LoggerFactory.getLogger("PaymentQueueMessageReceiver");

    @JmsListener(destination = "${jms.paymentQueueDestination}")
    public void receivePaymentQueueMessage(@Header(JmsHeaders.CORRELATION_ID) String id, PaymentQueueMessage message)
            throws JmsPaymentProcessingException {
        LOGGER.info(String.format("Message received with id: %s", id));

        try {
            PaymentAPIService paymentAPIService = paymentServiceFactory.getPaymentService(message.getBankCode());
            paymentAPIService.makePayment(message, id);
        }
        catch (ApiException exc) {
            PaymentProcessingErrorMapper mapper =
                    paymentProcessingErrorMapperFactory.getPaymentProcessingErrorMapper(exc.getResponseError());
            throw new JmsPaymentProcessingException(id, mapper.mapToPaymentProcessingError(exc.getResponseError()));
        }
        catch (BankNotSupportedException | InvalidPaymentResponseException | GenericException exc) {
            PaymentProcessingError paymentProcessingError = new PaymentProcessingError();
            paymentProcessingError.setType("Internal server error");
            paymentProcessingError.setDescription("No message available");
            throw new JmsPaymentProcessingException(id, paymentProcessingError);
        }
    }
}
