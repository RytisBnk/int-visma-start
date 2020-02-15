package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.AccountsListResponse;

public interface SwedbankAccountsService {
    AccountsListResponse getUserAccounts(String consentId, String accessToken, String psuUserAgent, String psuIP, String psuID)
            throws GenericException, ApiException;
}
