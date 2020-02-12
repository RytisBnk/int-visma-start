package lt.visma.starter.model.swedbank;

import java.util.List;

public class AccountsListResponse {
    private List<SwedbankAccount> accounts;

    public List<SwedbankAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<SwedbankAccount> accounts) {
        this.accounts = accounts;
    }
}
