package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Counterparty {
    private String id;
    @JsonAlias({"account_id"})
    private String accountId;
    @JsonAlias({"account_type"})
    private CounterpartyType type;

    public Counterparty(String id, String accountId, CounterpartyType type) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
    }

    public Counterparty(String accountId) {
        this.accountId = accountId;
    }

    public Counterparty() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public CounterpartyType getType() {
        return type;
    }

    public void setType(CounterpartyType type) {
        this.type = type;
    }
}
