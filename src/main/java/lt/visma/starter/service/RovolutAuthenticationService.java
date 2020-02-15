package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.revolut.RevolutAccessToken;

public interface RovolutAuthenticationService {
    String getJWTToken() throws GenericException;
    RevolutAccessToken getAccessToken(String jwtToken) throws GenericException, ApiException;
    RevolutAccessToken refreshAccessToken(String jwtToken) throws GenericException, ApiException;
}
