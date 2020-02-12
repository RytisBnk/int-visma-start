package lt.visma.starter.service;

import lt.visma.starter.model.swedbank.ConsentResponse;

public interface SwedbankAccountsService {
    ConsentResponse getUserConsent(String accessToken);
}
