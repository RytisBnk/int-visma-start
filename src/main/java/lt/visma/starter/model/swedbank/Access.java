package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonInclude;
import lt.visma.starter.model.swedbank.entity.AccountIBAN;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Access {
    private List<AccountIBAN> accounts;
    private String availableAccounts;
    private List<AccountIBAN> balances;
    private List<AccountIBAN> transactions;

    public Access(List<AccountIBAN> accounts, String availableAccounts, List<AccountIBAN> balances, List<AccountIBAN> transactions) {
        this.accounts = accounts;
        this.availableAccounts = availableAccounts;
        this.balances = balances;
        this.transactions = transactions;
    }

    public Access() {
    }

    public List<AccountIBAN> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountIBAN> accounts) {
        this.accounts = accounts;
    }

    public String getAvailableAccounts() {
        return availableAccounts;
    }

    public void setAvailableAccounts(String availableAccounts) {
        this.availableAccounts = availableAccounts;
    }

    public List<AccountIBAN> getBalances() {
        return balances;
    }

    public void setBalances(List<AccountIBAN> balances) {
        this.balances = balances;
    }

    public List<AccountIBAN> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountIBAN> transactions) {
        this.transactions = transactions;
    }
}
