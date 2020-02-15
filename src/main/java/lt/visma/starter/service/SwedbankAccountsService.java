package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.AccountsListResponse;
import lt.visma.starter.model.swedbank.ConsentResponse;

public interface SwedbankAccountsService {
    ConsentResponse getUserConsent(String accessToken,String psuUserAgent, String psuIP)
            throws GenericException, ApiException;
    AccountsListResponse getUserAccounts(String consentId, String accessToken, String psuUserAgent, String psuIP, String psuID)
            throws GenericException, ApiException;
}
