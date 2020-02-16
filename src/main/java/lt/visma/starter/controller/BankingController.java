package lt.visma.starter.controller;

import lt.visma.starter.exception.*;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.ConsentService;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import lt.visma.starter.service.factory.BankingAccountsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                             ConsentService consentService) {
        this.bankingAccountsServiceFactory = bankingAccountsServiceFactory;
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.consentService = consentService;
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
        return new ResponseEntity<>(consentService.createUserConsent(authenticationService.getAccessToken(params),params), HttpStatus.OK);
    }
}
