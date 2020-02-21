package lt.visma.starter.model.revolut.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import lt.visma.starter.model.revolut.CounterpartyType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Counterparty {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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
