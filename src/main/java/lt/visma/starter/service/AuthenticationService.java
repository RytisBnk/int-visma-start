package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;

import java.util.Map;

public interface AuthenticationService {
    String getAccessToken(Map<String, String> parameters)
            throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
