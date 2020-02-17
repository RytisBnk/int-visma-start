package lt.visma.starter.controller;

import lt.visma.starter.exception.*;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.*;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import lt.visma.starter.service.factory.BankingAccountsServiceFactory;
import lt.visma.starter.service.factory.PaymentServiceFactory;
import lt.visma.starter.service.factory.TransactionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @ExceptionHandler({SwedbankApiException.class})
    public ResponseEntity<Object> handleSwedbankErrors(SwedbankApiException exc) {
        return new ResponseEntity<>(exc.getResponseError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RevolutApiException.class})
    private ResponseEntity<Object> handleRevolutErrors(RevolutApiException exc) {
        return new ResponseEntity<>(exc.getResponseError(), HttpStatus.BAD_REQUEST);
    }

    @Autowired
    public BankingController(BankingAccountsServiceFactory bankingAccountsServiceFactory,
                             AuthenticationServiceFactory authenticationServiceFactory,
                             ConsentService consentService,
                             PaymentServiceFactory paymentServiceFactory,
                             TransactionServiceFactory transactionServiceFactory) {
        this.bankingAccountsServiceFactory = bankingAccountsServiceFactory;
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.consentService = consentService;
        this.paymentServiceFactory = paymentServiceFactory;
        this.transactionServiceFactory = transactionServiceFactory;
    }

    @PostMapping("/accounts")
    public List<BankingAccount> getBankingAccounts(@RequestBody Map<String, String> parameters,
                                                   @RequestParam String bankCode)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        BankingAccountsService bankingAccountsService = bankingAccountsServiceFactory.getBankingAccountsService(bankCode);
        String accessToken = authenticationService.getAccessToken(parameters);
        return bankingAccountsService.getBankingAccounts(accessToken, parameters);
    }

    @PostMapping("/consents")
    public ResponseEntity<ConsentResponse> getSwedbankConsent(@RequestParam String bankCode,
                                                              @RequestBody Map<String, String> params)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        return new ResponseEntity<>(consentService.createUserConsent(
                authenticationService.getAccessToken(params),params), HttpStatus.OK
        );
    }

    @PostMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createPayment(@RequestParam("bankCode") String bankCode,
                                                         @RequestBody RevolutPaymentRequest paymentRequest)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        PaymentService paymentService = paymentServiceFactory.getPaymentService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(new HashMap<>());
        PaymentResponse paymentResponse = paymentService.makePayment(accessToken, paymentRequest);
        return new ResponseEntity<>(transactionService.getPaymentTransaction(accessToken, paymentResponse), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam("bankCode") String bankCode,
                                                             @RequestParam("from") String from,
                                                             @RequestParam("to") String to)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(new HashMap<>());
        return new ResponseEntity<>(transactionService.getTransactions(accessToken, from, to), HttpStatus.OK);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") String id,
                                                          @RequestParam("bankCode") String bankCode)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(new HashMap<>());
        return new ResponseEntity<>(transactionService.getTransactionById(accessToken, id), HttpStatus.OK);
    }
}
