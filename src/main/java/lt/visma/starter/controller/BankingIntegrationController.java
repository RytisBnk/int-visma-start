package lt.visma.starter.controller;

import lt.visma.starter.service.RevolutAccountsService;
import lt.visma.starter.service.RovolutAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/banking")
public class BankingIntegrationController {
    private RevolutAccountsService revolutAccountsService;
    private RovolutAuthenticationService rovolutAuthenticationService;

    @Autowired
    public BankingIntegrationController(RevolutAccountsService revolutAccountsService,
                                        RovolutAuthenticationService rovolutAuthenticationService) {
        this.revolutAccountsService = revolutAccountsService;
        this.rovolutAuthenticationService = rovolutAuthenticationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/accounts", produces = "application/json")
    public String getListOfAccounts() {
        return revolutAccountsService.getAccounts("oa_sand_vsfMyVDZ71P0bTeZ5uJGJb5ODogo6EwtlfmEIdoqQOc");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jwt-token")
    public String getJwtToken() {
        return rovolutAuthenticationService.getJWTToken();
    }
}
