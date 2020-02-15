package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.TokenResponse;

public interface SwedBankAuthenticationService {
    TokenResponse getAccessToken(String psuID, String scaMethod)
            throws GenericException, ApiException;
}
