package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.SwedbankApiException;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.ConsentService;
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

    private SwedBankAuthenticationService swedBankAuthenticationService;
    private SwedbankAccountsService swedbankAccountsService;
    private ConsentService consentService;

    @Autowired
    public SwedbankBankingController(SwedBankAuthenticationService swedBankAuthenticationService,
                                     SwedbankAccountsService swedbankAccountsService,
                                     ConsentService consentService) {
        this.swedBankAuthenticationService = swedBankAuthenticationService;
        this.swedbankAccountsService = swedbankAccountsService;
        this.consentService = consentService;
    }

    @GetMapping(value = "/access-token")
    public ResponseEntity<TokenResponse> getAccessToken(@RequestHeader("PSU-ID") String psuID,
                                                        @RequestHeader("Sca-Method") String scaMethod) throws GenericException, ApiException {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken(psuID, scaMethod);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/consent")
    public ResponseEntity<Object> getUserConsent(@RequestHeader("User-Agent") String psuUserAgent,
                                                 @RequestHeader("Host") String psuIPAddress,
                                                 @RequestHeader("PSU-ID") String psuID,
                                                 @RequestHeader("Sca-Method") String scaMethod) throws GenericException, ApiException {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken(psuID, scaMethod);
        ConsentResponse consentResponse = consentService
                .createUserConsent(tokenResponse.getAccessToken(), psuUserAgent, psuIPAddress);
        return new ResponseEntity<>(consentResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<Object> getUserAccounts(@RequestParam("consent-id") String consentId,
                                                  @RequestHeader("User-Agent") String psuUserAgent,
                                                  @RequestHeader("Host") String psuIPAddress,
                                                  @RequestHeader("PSU-ID") String psuID,
                                                  @RequestHeader("Sca-Method") String scaMethod) throws GenericException, ApiException {
        TokenResponse tokenResponse = swedBankAuthenticationService.getAccessToken(psuID, scaMethod);
        AccountsListResponse swedbankAccounts = swedbankAccountsService
                .getUserAccounts(consentId, tokenResponse.getAccessToken(), psuUserAgent, psuIPAddress, psuID);
        return new ResponseEntity<>(swedbankAccounts, HttpStatus.OK);
    }
}
