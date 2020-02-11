package lt.visma.starter.service;

import lt.visma.starter.model.RevolutAccessToken;

public interface RovolutAuthenticationService {
    String getJWTToken();
    RevolutAccessToken getAccessToken();
}
