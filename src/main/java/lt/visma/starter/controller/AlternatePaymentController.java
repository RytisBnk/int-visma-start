package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.*;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.SavedPaymentService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import lt.visma.starter.service.factory.PaymentServiceFactory;
import lt.visma.starter.service.factory.TransactionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/alt/payments")
public class AlternatePaymentController {
    private AuthenticationServiceFactory authenticationServiceFactory;
    private PaymentServiceFactory paymentServiceFactory;
    private TransactionServiceFactory transactionServiceFactory;
    private SavedPaymentService savedPaymentService;

    @Autowired
    public AlternatePaymentController(AuthenticationServiceFactory authenticationServiceFactory,
                                      PaymentServiceFactory paymentServiceFactory,
                                      TransactionServiceFactory transactionServiceFactory,
                                      SavedPaymentService savedPaymentService) {
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.paymentServiceFactory = paymentServiceFactory;
        this.transactionServiceFactory = transactionServiceFactory;
        this.savedPaymentService = savedPaymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getSavedPayments() {
        return new ResponseEntity<>(savedPaymentService.getPayments(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(@RequestParam String bankCode,
                                               @RequestHeader Map<String, String> headers,
                                               @RequestBody PaymentRequest paymentRequest)
            throws BankNotSupportedException, GenericException, ApiException, InvalidTransactionException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        PaymentService paymentService = paymentServiceFactory.getPaymentService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);

        String accessToken = authenticationService.getAccessToken(headers);
        PaymentResponse paymentResponse = paymentService.makePayment(accessToken, paymentRequest);
        Transaction transaction = transactionService.getTransactionById(accessToken, paymentResponse.getId(), bankCode);
        return new ResponseEntity<>(savedPaymentService.savePayment(transaction), HttpStatus.CREATED);
    }
}
