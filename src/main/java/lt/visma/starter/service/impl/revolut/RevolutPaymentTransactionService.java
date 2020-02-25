package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
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
public class RevolutPaymentTransactionService implements PaymentTransactionService {
    private AuthenticationService revolutAuthenticationService;
    private PaymentService revolutPaymentService;
    private TransactionService revolutTransactionService;
    private PaymentRepository paymentRepository;
    private PaymentMapper revolutPaymentMapper;
    private RevolutConfigurationProperties configurationProperties;

    public RevolutPaymentTransactionService(AuthenticationService revolutAuthenticationService,
                                            PaymentService revolutPaymentService,
                                            TransactionService revolutTransactionService,
                                            PaymentRepository paymentRepository,
                                            PaymentMapper revolutPaymentMapper,
                                            RevolutConfigurationProperties configurationProperties) {
        this.revolutAuthenticationService = revolutAuthenticationService;
        this.revolutPaymentService = revolutPaymentService;
        this.revolutTransactionService = revolutTransactionService;
        this.paymentRepository = paymentRepository;
        this.revolutPaymentMapper = revolutPaymentMapper;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public Payment submitPayment(Map<String, String> authParams, PaymentRequest paymentRequest, String bankCode)
            throws GenericException, ApiException, InvalidTransactionException, InvalidPaymentResponseException {
        String accessToken = revolutAuthenticationService.getAccessToken(authParams);

        PaymentSubmission paymentSubmission = revolutPaymentService.makePayment(paymentRequest, accessToken);
        Transaction transaction = revolutTransactionService.getTransactionById(paymentSubmission.getPaymentId(), bankCode, accessToken);

        return paymentRepository.save(revolutPaymentMapper.mapToPersistentPaymentObject(transaction));
    }

    @Override
    public Payment getPaymentById(String id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getBankName().equals("Revolut"))
                .collect(Collectors.toList());
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }
}
