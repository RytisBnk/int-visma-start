package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.ConsentService;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/consents")
public class ConsentsController {
    private AuthenticationServiceFactory authenticationServiceFactory;
    private ConsentService consentService;

    @Autowired
    public ConsentsController(AuthenticationServiceFactory authenticationServiceFactory, ConsentService consentService) {
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.consentService = consentService;
    }

    @PostMapping
    public ResponseEntity<ConsentResponse> getSwedbankConsent(@RequestParam String bankCode,
                                                              @RequestHeader Map<String, String> params)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        return new ResponseEntity<>(consentService.createUserConsent(
                authenticationService.getAccessToken(params),params), HttpStatus.OK
        );
    }
}
