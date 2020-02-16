package lt.visma.starter.service;

import lt.visma.starter.model.revolut.RevolutAccessToken;
import lt.visma.starter.model.revolut.RevolutAccount;

import java.util.List;

public interface RevolutAccountsService {
    List<RevolutAccount> getAccounts(RevolutAccessToken accessToken);
}
