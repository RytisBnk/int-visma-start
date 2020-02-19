package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;
import lt.visma.starter.model.BankingAccount;

public class RevolutAccount implements BankingAccount {
    private String id;
    private String name;
    private double balance;
    private String currency;
    private String state;
    @JsonAlias({"public"})
    private boolean isPublic;
    @JsonAlias({"created_at"})
    private String createdAt;
    @JsonAlias({"updated_at"})
    private String updatedAt;

    public RevolutAccount(String id, String name, double balance, String currency, String state, boolean isPublic, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
        this.state = state;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public RevolutAccount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
