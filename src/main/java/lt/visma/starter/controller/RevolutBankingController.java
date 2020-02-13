package lt.visma.starter.controller;

import lt.visma.starter.exception.RevolutApiException;
import lt.visma.starter.model.revolut.RevolutAccessToken;
import lt.visma.starter.model.revolut.RevolutAccount;
import lt.visma.starter.model.revolut.ResponseError;
import lt.visma.starter.service.RevolutAccountsService;
import lt.visma.starter.service.RovolutAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/revolut")
public class RevolutBankingController {
    @ExceptionHandler
    private ResponseEntity<ResponseError> handleException(RevolutApiException exc) {
        return new ResponseEntity<>(exc.getResponseError(), HttpStatus.BAD_REQUEST);
    }

    private RevolutAccountsService revolutAccountsService;
    private RovolutAuthenticationService rovolutAuthenticationService;

    @Autowired
    public RevolutBankingController(RevolutAccountsService revolutAccountsService,
                                    RovolutAuthenticationService rovolutAuthenticationService) {
        this.revolutAccountsService = revolutAccountsService;
        this.rovolutAuthenticationService = rovolutAuthenticationService;
    }

    @GetMapping(value = "/accounts", produces = "application/json")
    public ResponseEntity<List<RevolutAccount>> getListOfAccounts() {
        RevolutAccessToken accessToken = rovolutAuthenticationService.refreshAccessToken();
        return new ResponseEntity<>(revolutAccountsService.getAccounts(accessToken), HttpStatus.OK);
    }
}
