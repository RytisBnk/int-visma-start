package lt.visma.starter.service.impl;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.JmsPaymentProcessingException;
import lt.visma.starter.model.entity.PaymentQueueEntry;
import lt.visma.starter.model.entity.QueueEntryState;
import lt.visma.starter.repository.PaymentQueueEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

import java.util.Optional;

@Service
public class JmsErrorHandlerService implements ErrorHandler {
    private final PaymentQueueEntryRepository paymentQueueEntryRepository;

    private final Logger LOGGER = LoggerFactory.getLogger("JmsErrorHandlerService");

    public JmsErrorHandlerService(PaymentQueueEntryRepository paymentQueueEntryRepository) {
        this.paymentQueueEntryRepository = paymentQueueEntryRepository;
    }

    @Override
    public void handleError(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause instanceof JmsPaymentProcessingException) {
            JmsPaymentProcessingException exception = (JmsPaymentProcessingException) cause;

            LOGGER.info(
                    String.format("JmsPaymentProcessingException occurred for payment queue entry %s", exception.getPaymentQueueId())
            );

            Optional<PaymentQueueEntry> paymentQueueEntryOptional =
                    paymentQueueEntryRepository.findById(exception.getPaymentQueueId());
            if (paymentQueueEntryOptional.isPresent()) {
                PaymentQueueEntry queueEntry = paymentQueueEntryOptional.get();
                queueEntry.setStatus(QueueEntryState.FAILED);
                queueEntry.setError(exception.getProcessingError());
                paymentQueueEntryRepository.save(queueEntry);
            }
        }
    }
}
