package lt.visma.starter.service;

import lt.visma.starter.model.RevolutAccessToken;
import lt.visma.starter.model.RevolutAccount;

import java.util.List;

public interface RevolutAccountsService {
    List<RevolutAccount> getAccounts(RevolutAccessToken accessToken);
}
