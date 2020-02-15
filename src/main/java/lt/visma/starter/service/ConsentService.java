package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.ConsentResponse;

public interface ConsentService {
    ConsentResponse createUserConsent(String accessToken, String psuUserAgent, String psuIP)
            throws GenericException, ApiException;
}
