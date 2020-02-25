package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.factory.BankingAccountsServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/accounts")
public class AccountsController {
    private final BankingAccountsServiceFactory bankingAccountsServiceFactory;

    @Autowired
    public AccountsController(BankingAccountsServiceFactory bankingAccountsServiceFactory) {
        this.bankingAccountsServiceFactory = bankingAccountsServiceFactory;
    }

    @GetMapping
    public List<BankingAccount> getBankingAccounts(@RequestHeader Map<String, String> parameters,
                                                   @RequestParam String bankCode)
            throws BankNotSupportedException, GenericException, ApiException {
        BankingAccountsService bankingAccountsService = bankingAccountsServiceFactory.getBankingAccountsService(bankCode);
        return bankingAccountsService.getBankingAccounts(parameters);
    }
}
