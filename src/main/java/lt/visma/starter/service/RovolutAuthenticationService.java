package lt.visma.starter.service;

import lt.visma.starter.model.revolut.RevolutAccessToken;

public interface RovolutAuthenticationService {
    String getJWTToken();
    RevolutAccessToken getAccessToken();
    RevolutAccessToken refreshAccessToken();
}
