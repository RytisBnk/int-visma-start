package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.ConsentService;
import lt.visma.starter.service.factory.ConsentServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/consents")
public class ConsentsController {
    private final ConsentServiceFactory consentServiceFactory;

    @Autowired
    public ConsentsController(ConsentServiceFactory consentServiceFactory) {
        this.consentServiceFactory = consentServiceFactory;
    }

    @PostMapping
    public ResponseEntity<ConsentResponse> getSwedbankConsent(@RequestParam String bankCode,
                                                              @RequestHeader Map<String, String> params)
            throws BankNotSupportedException, GenericException, ApiException {
        ConsentService consentService = consentServiceFactory.getConsentService(bankCode);

        return new ResponseEntity<>(consentService.createUserConsent(params), HttpStatus.OK
        );
    }
}
