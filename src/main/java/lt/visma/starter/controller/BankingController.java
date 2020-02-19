package lt.visma.starter.controller;

import lt.visma.starter.exception.*;
import lt.visma.starter.model.*;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.*;
import lt.visma.starter.service.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/banking")
public class BankingController {
    private BankingAccountsServiceFactory bankingAccountsServiceFactory;
    private AuthenticationServiceFactory authenticationServiceFactory;
    private ConsentService consentService;
    private PaymentServiceFactory paymentServiceFactory;
    private TransactionServiceFactory transactionServiceFactory;
    private SavedTransactionServiceFactory savedTransactionServiceFactory;

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ResponseError> handleExternalApiError(ApiException exc) {
       return new ResponseEntity<>(exc.getResponseError(), HttpStatus.BAD_REQUEST);
    }

    @Autowired
    public BankingController(BankingAccountsServiceFactory bankingAccountsServiceFactory,
                             AuthenticationServiceFactory authenticationServiceFactory,
                             ConsentService consentService,
                             PaymentServiceFactory paymentServiceFactory,
                             TransactionServiceFactory transactionServiceFactory,
                             SavedTransactionServiceFactory savedTransactionServiceFactory) {
        this.bankingAccountsServiceFactory = bankingAccountsServiceFactory;
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.consentService = consentService;
        this.paymentServiceFactory = paymentServiceFactory;
        this.transactionServiceFactory = transactionServiceFactory;
        this.savedTransactionServiceFactory = savedTransactionServiceFactory;
    }

    @PostMapping("/accounts")
    public List<BankingAccount> getBankingAccounts(@RequestHeader Map<String, String> parameters,
                                                   @RequestParam String bankCode)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        BankingAccountsService bankingAccountsService = bankingAccountsServiceFactory.getBankingAccountsService(bankCode);
        String accessToken = authenticationService.getAccessToken(parameters);
        return bankingAccountsService.getBankingAccounts(accessToken, parameters);
    }

    @PostMapping("/consents")
    public ResponseEntity<ConsentResponse> getSwedbankConsent(@RequestParam String bankCode,
                                                              @RequestHeader Map<String, String> params)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        return new ResponseEntity<>(consentService.createUserConsent(
                authenticationService.getAccessToken(params),params), HttpStatus.OK
        );
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Transaction>> getPayments(@RequestParam String bankCode) throws BankNotSupportedException {
        SavedTransactionsService savedTransactionsService = savedTransactionServiceFactory.getSavedTransactionService(bankCode);
        return new ResponseEntity<>(savedTransactionsService.getAllTransactions(), HttpStatus.OK);
    }

    @PostMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam("bankCode") String bankCode,
                                                             @RequestParam("from") String from,
                                                             @RequestParam("to") String to,
                                                             @RequestHeader Map<String, String> headers)
            throws BankNotSupportedException, GenericException, ApiException, OperationNotSupportedException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(headers);
        return new ResponseEntity<>(transactionService.getTransactions(accessToken, from, to), HttpStatus.OK);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") String id,
                                                          @RequestParam("bankCode") String bankCode,
                                                          @RequestHeader Map<String, String> headers)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(headers);
        return new ResponseEntity<>(transactionService.getTransactionById(accessToken, id, bankCode), HttpStatus.OK);
    }
}
