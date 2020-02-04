package lt.visma.starter.controller;

import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.BankingAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/banking")
public class BankingIntegrationController {
    private BankingAccountsService bankingAccountsService;
    private BankingAuthenticationService bankingAuthenticationService;

    @Autowired
    public BankingIntegrationController(BankingAccountsService bankingAccountsService,
                                        BankingAuthenticationService bankingAuthenticationService) {
        this.bankingAccountsService = bankingAccountsService;
        this.bankingAuthenticationService = bankingAuthenticationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/accounts", produces = "application/json")
    public String getListOfAccounts() {
        return bankingAccountsService.getAccounts("oa_sand_vsfMyVDZ71P0bTeZ5uJGJb5ODogo6EwtlfmEIdoqQOc");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jwt-token")
    public String getJwtToken() {
        return bankingAuthenticationService.getJWTToken();
    }
}
