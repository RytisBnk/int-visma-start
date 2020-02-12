package lt.visma.starter.service;

import lt.visma.starter.model.swedbank.DecoupledAuthResponse;
import lt.visma.starter.model.swedbank.TokenResponse;

public interface SwedBankAuthenticationService {
    TokenResponse getAccessToken();
    DecoupledAuthResponse getAuthorizationID();
}
