package lt.visma.starter.controller;

import lt.visma.starter.exception.*;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.ConsentService;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import lt.visma.starter.service.factory.BankingAccountsServiceFactory;
import lt.visma.starter.service.factory.PaymentServiceFactory;
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
                             PaymentServiceFactory paymentServiceFactory) {
        this.bankingAccountsServiceFactory = bankingAccountsServiceFactory;
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.consentService = consentService;
        this.paymentServiceFactory = paymentServiceFactory;
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
    public ResponseEntity<PaymentResponse> createPayment(@RequestParam("bankCode") String bankCode,
                                                         @RequestBody RevolutPaymentRequest paymentRequest)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        PaymentService paymentService = paymentServiceFactory.getPaymentService(bankCode);
        String accessToken = authenticationService.getAccessToken(new HashMap<>());
        return new ResponseEntity<>(paymentService.makePayment(accessToken, paymentRequest), HttpStatus.OK);
    }
}
