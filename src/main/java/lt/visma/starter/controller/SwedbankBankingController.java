package lt.visma.starter.controller;

import lt.visma.starter.exception.SwedbankApiException;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.SwedBankAuthenticationService;
import lt.visma.starter.service.SwedbankAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/v1/swedbank")
@RestController
public class SwedbankBankingController {
    @ExceptionHandler({SwedbankApiException.class})
    private ResponseEntity<ResponseError> handleException(SwedbankApiException error) {
        return new ResponseEntity<>(error.getResponseError(), HttpStatus.BAD_REQUEST);
    }

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
    public ResponseEntity<Object> getuserConsent() {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken();
        ConsentResponse consentResponse = swedbankAccountsService.getUserConsent(tokenResponse.getAccessToken());
        return new ResponseEntity<>(consentResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<Object> getUserAccounts(@RequestParam("consent-id") String consentId) {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken();
        AccountsListResponse swedbankAccounts = swedbankAccountsService.getUserAccounts(consentId, tokenResponse.getAccessToken());
        return new ResponseEntity<>(swedbankAccounts, HttpStatus.OK);
    }
}
