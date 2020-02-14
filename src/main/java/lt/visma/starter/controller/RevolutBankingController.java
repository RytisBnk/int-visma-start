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
        String jwtToken = rovolutAuthenticationService.getJWTToken();
        RevolutAccessToken accessToken = rovolutAuthenticationService.refreshAccessToken(jwtToken);
        return new ResponseEntity<>(revolutAccountsService.getAccounts(accessToken), HttpStatus.OK);
    }

    @GetMapping(value = "/jwt-token")
    public ResponseEntity<String> getJwtToken() {
        return new ResponseEntity<>(rovolutAuthenticationService.getJWTToken(), HttpStatus.OK);
    }

    @GetMapping(value = "/access-token")
    public ResponseEntity<RevolutAccessToken> getRefreshedAccessToken() {
        String jwtToken = rovolutAuthenticationService.getJWTToken();
        RevolutAccessToken accessToken = rovolutAuthenticationService.refreshAccessToken(jwtToken);
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }
}
