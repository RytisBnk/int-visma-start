package lt.visma.starter.service;

import lt.visma.starter.model.swedbank.AccountsListResponse;
import lt.visma.starter.model.swedbank.ConsentResponse;

public interface SwedbankAccountsService {
    ConsentResponse getUserConsent(String accessToken,String psuUserAgent, String psuIP);
    AccountsListResponse getUserAccounts(String consentId, String accessToken, String psuUserAgent, String psuIP);
}
