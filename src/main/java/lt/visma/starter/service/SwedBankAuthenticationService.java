package lt.visma.starter.service;

import lt.visma.starter.model.swedbank.TokenResponse;

public interface SwedBankAuthenticationService {
    TokenResponse getAccessToken(String psuID);
}
