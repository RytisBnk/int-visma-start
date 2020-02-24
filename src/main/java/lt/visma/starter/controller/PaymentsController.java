package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.SavedTransactionsService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import lt.visma.starter.service.factory.PaymentServiceFactory;
import lt.visma.starter.service.factory.SavedTransactionServiceFactory;
import lt.visma.starter.service.factory.TransactionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/payments")
public class PaymentsController {
    private AuthenticationServiceFactory authenticationServiceFactory;
    private PaymentServiceFactory paymentServiceFactory;
    private TransactionServiceFactory transactionServiceFactory;
    private SavedTransactionServiceFactory savedTransactionServiceFactory;


    public PaymentsController(AuthenticationServiceFactory authenticationServiceFactory,
                              PaymentServiceFactory paymentServiceFactory,
                              TransactionServiceFactory transactionServiceFactory,
                              SavedTransactionServiceFactory savedTransactionServiceFactory) {
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.paymentServiceFactory = paymentServiceFactory;
        this.transactionServiceFactory = transactionServiceFactory;
        this.savedTransactionServiceFactory = savedTransactionServiceFactory;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getPayments(@RequestParam String bankCode) throws BankNotSupportedException {
        SavedTransactionsService savedTransactionsService = savedTransactionServiceFactory.getSavedTransactionService(bankCode);
        return new ResponseEntity<>(savedTransactionsService.getAllTransactions(), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createPayment(@RequestParam("bankCode") String bankCode,
                                                     @RequestBody PaymentRequest paymentRequest,
                                                     @RequestHeader Map<String, String> headers)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        PaymentService paymentService = paymentServiceFactory.getPaymentService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        SavedTransactionsService savedTransactionsService = savedTransactionServiceFactory.getSavedTransactionService(bankCode);

        String accessToken = authenticationService.getAccessToken(headers);
        PaymentResponse paymentResponse = paymentService.makePayment(accessToken, paymentRequest);
        Transaction paymentTransaction = transactionService.getTransactionById(accessToken, paymentResponse.getId(), bankCode);
        return new ResponseEntity<>(savedTransactionsService.saveTransaction(paymentTransaction), HttpStatus.CREATED);
    }
}
