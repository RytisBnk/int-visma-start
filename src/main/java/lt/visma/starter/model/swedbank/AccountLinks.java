package lt.visma.starter.model.swedbank;

public class AccountLinks {
    private ResponseLink balances;
    private ResponseLink transactions;

    public ResponseLink getBalances() {
        return balances;
    }

    public void setBalances(ResponseLink balances) {
        this.balances = balances;
    }

    public ResponseLink getTransactions() {
        return transactions;
    }

    public void setTransactions(ResponseLink transactions) {
        this.transactions = transactions;
    }
}
