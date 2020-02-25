package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.mapper.PaymentMapper;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.model.entity.PaymentSubmission;
import lt.visma.starter.repository.PaymentRepository;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.PaymentTransactionService;
import lt.visma.starter.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SwedbankPaymentTransactionService implements PaymentTransactionService {
    private final SwedbankConfigurationProperties configurationProperties;
    private final AuthenticationService swedbankAuthenticationService;
    private final PaymentService swedbankPaymentService;
    private final PaymentRepository paymentRepository;
    private final TransactionService swedbankTransactionService;
    private final PaymentMapper swedbankPaymentMapper;

    public SwedbankPaymentTransactionService(SwedbankConfigurationProperties configurationProperties,
                                             AuthenticationService swedbankAuthenticationService,
                                             PaymentService swedbankPaymentService,
                                             PaymentRepository paymentRepository,
                                             TransactionService swedbankTransactionService,
                                             PaymentMapper swedbankPaymentMapper) {
        this.configurationProperties = configurationProperties;
        this.swedbankAuthenticationService = swedbankAuthenticationService;
        this.swedbankPaymentService = swedbankPaymentService;
        this.paymentRepository = paymentRepository;
        this.swedbankTransactionService = swedbankTransactionService;
        this.swedbankPaymentMapper = swedbankPaymentMapper;
    }

    @Override
    public Payment submitPayment(Map<String, String> authParams, PaymentRequest paymentRequest, String bankCode)
            throws GenericException, ApiException, InvalidTransactionException, InvalidPaymentResponseException {
        String accessToken = swedbankAuthenticationService.getAccessToken(authParams);

        PaymentSubmission paymentSubmission = swedbankPaymentService.makePayment(paymentRequest, accessToken);
        Transaction transaction = swedbankTransactionService.getTransactionById(paymentSubmission.getPaymentId(), bankCode, accessToken);

        return paymentRepository.save(swedbankPaymentMapper.mapToPersistentPaymentObject(transaction));
    }

    @Override
    public Payment getPaymentById(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getBankName().equals("Swedbank"))
                .collect(Collectors.toList());
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }
}
