package lt.visma.starter.controller;

import lt.visma.starter.model.RevolutAccessToken;
import lt.visma.starter.model.RevolutAccount;
import lt.visma.starter.service.RevolutAccountsService;
import lt.visma.starter.service.RovolutAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/revolut")
public class RevolutBankingController {
    private RevolutAccountsService revolutAccountsService;
    private RovolutAuthenticationService rovolutAuthenticationService;

    @Autowired
    public RevolutBankingController(RevolutAccountsService revolutAccountsService,
                                    RovolutAuthenticationService rovolutAuthenticationService) {
        this.revolutAccountsService = revolutAccountsService;
        this.rovolutAuthenticationService = rovolutAuthenticationService;
    }

    @GetMapping(value = "/accounts", produces = "application/json")
    public List<RevolutAccount> getListOfAccounts() {
        RevolutAccessToken accessToken = rovolutAuthenticationService.refreshAccessToken();
        return revolutAccountsService.getAccounts(accessToken);
    }

    @GetMapping(value = "/auth/jwt-token")
    public String getJwtToken() {
        return rovolutAuthenticationService.getJWTToken();
    }

    @GetMapping(value = "/auth/access-token", produces = "application/json")
    public RevolutAccessToken getRevolutAccessToken() {
        return rovolutAuthenticationService.getAccessToken();
    }

    @GetMapping(value = "/auth/access-token/refresh")
    public RevolutAccessToken refreshAccessToken() {
        return rovolutAuthenticationService.refreshAccessToken();
    }
}
