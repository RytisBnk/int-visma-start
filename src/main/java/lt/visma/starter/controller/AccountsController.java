package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/accounts")
public class AccountsController {
    private BankingAccountsServiceFactory bankingAccountsServiceFactory;
    private AuthenticationServiceFactory authenticationServiceFactory;

    @Autowired
    public AccountsController(BankingAccountsServiceFactory bankingAccountsServiceFactory,
                              AuthenticationServiceFactory authenticationServiceFactory) {
        this.bankingAccountsServiceFactory = bankingAccountsServiceFactory;
        this.authenticationServiceFactory = authenticationServiceFactory;
    }

    @GetMapping
    public List<BankingAccount> getBankingAccounts(@RequestHeader Map<String, String> parameters,
                                                   @RequestParam String bankCode)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        BankingAccountsService bankingAccountsService = bankingAccountsServiceFactory.getBankingAccountsService(bankCode);
        String accessToken = authenticationService.getAccessToken(parameters);
        return bankingAccountsService.getBankingAccounts(accessToken, parameters);
    }
}
