package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.jms.PaymentQueueMessageSender;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.PaymentQueueEntry;
import lt.visma.starter.model.entity.QueueEntryState;
import lt.visma.starter.model.swedbank.SwedbankPaymentRequest;
import lt.visma.starter.repository.PaymentQueueEntryRepository;
import lt.visma.starter.service.PaymentQueueService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class SwedbankPaymentQueueService implements PaymentQueueService {
    private final PaymentQueueMessageSender paymentQueueMessageSender;
    private final PaymentQueueEntryRepository paymentQueueEntryRepository;
    private final SwedbankConfigurationProperties configurationProperties;

    public SwedbankPaymentQueueService(PaymentQueueMessageSender paymentQueueMessageSender,
                                       PaymentQueueEntryRepository paymentQueueEntryRepository,
                                       SwedbankConfigurationProperties configurationProperties) {
        this.paymentQueueMessageSender = paymentQueueMessageSender;
        this.paymentQueueEntryRepository = paymentQueueEntryRepository;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public PaymentQueueEntry submitPaymentToQueue(String bankCode, Map<String, String> authParams, PaymentRequest paymentRequest) {
        if (!authParams.containsKey("psu-id") || !authParams.containsKey("sca-method")) {
            throw new IllegalArgumentException("Required auth params: psu-id, sca-method");
        }

        PaymentQueueEntry queueEntry = paymentQueueEntryRepository.save(getQueueEntryObject(bankCode));

        paymentQueueMessageSender.sendPaymentQueueMessage(queueEntry.getId(), bankCode, authParams, paymentRequest);

        return queueEntry;
    }

    @Override
    public PaymentQueueEntry getQueueEntryById(String id) {
        return paymentQueueEntryRepository.findById(id).orElse(null);
    }

    private PaymentQueueEntry getQueueEntryObject(String bankCode) {
        PaymentQueueEntry queueEntry = new PaymentQueueEntry();
        queueEntry.setStatus(QueueEntryState.PROCESSING);
        queueEntry.setBankCode(bankCode);

        return queueEntry;
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }
}
