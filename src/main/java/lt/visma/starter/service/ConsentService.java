package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.ConsentResponse;

import java.util.Map;

public interface ConsentService {
    ConsentResponse createUserConsent(String accessToken, Map<String, String> parameters)
            throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
