package lt.visma.starter.controller;

import lt.visma.starter.service.BankingAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/banking")
public class BankingIntegrationController {
    private BankingAccountsService bankingAccountsService;

    @Autowired
    public BankingIntegrationController(BankingAccountsService bankingAccountsService) {
        this.bankingAccountsService = bankingAccountsService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/accounts")
    public String getListOfAccounts() {
        return bankingAccountsService.getAccounts("oa_sand_vsfMyVDZ71P0bTeZ5uJGJb5ODogo6EwtlfmEIdoqQOc");
    }
}
