package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.jms.PaymentQueueMessageSender;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.PaymentQueueEntry;
import lt.visma.starter.model.entity.QueueEntryState;
import lt.visma.starter.repository.PaymentQueueEntryRepository;
import lt.visma.starter.service.PaymentQueueService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class RevolutPaymentQueueService implements PaymentQueueService {
    private RevolutConfigurationProperties configurationProperties;
    private PaymentQueueEntryRepository paymentQueueEntryRepository;
    private PaymentQueueMessageSender paymentQueueMessageSender;


    public RevolutPaymentQueueService(RevolutConfigurationProperties configurationProperties,
                                      PaymentQueueEntryRepository paymentQueueEntryRepository,
                                      PaymentQueueMessageSender paymentQueueMessageSender) {
        this.configurationProperties = configurationProperties;
        this.paymentQueueEntryRepository = paymentQueueEntryRepository;
        this.paymentQueueMessageSender = paymentQueueMessageSender;
    }

    @Override
    public PaymentQueueEntry submitPaymentToQueue(String bankCode, Map<String, String> authParams, PaymentRequest paymentRequest) {
        PaymentQueueEntry paymentQueueEntry = paymentQueueEntryRepository.save(getQueueEntryObject(bankCode));

        paymentQueueMessageSender.sendPaymentQueueMessage(paymentQueueEntry.getId(), bankCode, authParams, paymentRequest);

        return paymentQueueEntry;
    }

    @Override
    public PaymentQueueEntry getQueueEntryById(String id) {
        return paymentQueueEntryRepository.findById(id).orElse(null);
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private PaymentQueueEntry getQueueEntryObject(String bankCode) {
        PaymentQueueEntry queueEntry = new PaymentQueueEntry();
        queueEntry.setStatus(QueueEntryState.PROCESSING);
        queueEntry.setBankCode(bankCode);

        return queueEntry;
    }
}

