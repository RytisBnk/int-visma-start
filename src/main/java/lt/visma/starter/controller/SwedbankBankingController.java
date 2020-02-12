package lt.visma.starter.controller;

import lt.visma.starter.model.swedbank.AuthorizationCodeResponse;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.model.swedbank.DecoupledAuthResponse;
import lt.visma.starter.model.swedbank.TokenResponse;
import lt.visma.starter.service.SwedBankAuthenticationService;
import lt.visma.starter.service.SwedbankAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/v1/swedbank")
@RestController
public class SwedbankBankingController {
    @Autowired
    private SwedBankAuthenticationService swedBankAuthenticationService;

    @Autowired
    private SwedbankAccountsService swedbankAccountsService;

    @GetMapping(value = "/access-token")
    public ResponseEntity<TokenResponse> getAccessToken() {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken();
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/consent")
    public ResponseEntity<ConsentResponse> getuserConsent() {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken();
        ConsentResponse consentResponse = swedbankAccountsService.getUserConsent(tokenResponse.getAccessToken());
        return new ResponseEntity<>(consentResponse, HttpStatus.OK);
    }
}