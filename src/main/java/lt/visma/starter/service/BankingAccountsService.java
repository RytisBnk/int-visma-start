package lt.visma.starter.service;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;

import java.util.List;
import java.util.Map;

public interface BankingAccountsService {
    List<BankingAccount> getBankingAccounts(Map<String, String> parameters)
            throws GenericException, ApiException;
    boolean supportsBank(String bankCode);
}
