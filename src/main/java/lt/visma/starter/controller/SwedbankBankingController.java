package lt.visma.starter.controller;

import lt.visma.starter.model.swedbank.AuthorizationCodeResponse;
import lt.visma.starter.model.swedbank.DecoupledAuthResponse;
import lt.visma.starter.model.swedbank.TokenResponse;
import lt.visma.starter.service.SwedBankAuthenticationService;
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

    @GetMapping(value = "/access-token")
    public ResponseEntity<TokenResponse> getAccessToken() {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken();
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}
