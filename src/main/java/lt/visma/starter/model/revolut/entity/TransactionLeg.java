package lt.visma.starter.model.revolut.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TransactionLeg {
    @JsonAlias({"leg_id"})
    @Id
    private String legId;
    private double amount;
    private String currency;
    @JsonAlias({"bill_amount"})
    private String billAmount;
    @JsonAlias({"bill_currency"})
    private String billCurrency;
    @JsonAlias({"account_id"})
    private String accountId;
    @OneToOne(cascade = CascadeType.ALL)
    private Counterparty counterparty;
    private String description;
    private double balance;

    public TransactionLeg(String legId, double amount, String currency, String billAmount, String billCurrency, String accountId, Counterparty counterparty, String description, double balance) {
        this.legId = legId;
        this.amount = amount;
        this.currency = currency;
        this.billAmount = billAmount;
        this.billCurrency = billCurrency;
        this.accountId = accountId;
        this.counterparty = counterparty;
        this.description = description;
        this.balance = balance;
    }

    public TransactionLeg() {
    }

    public String getLegId() {
        return legId;
    }

    public void setLegId(String legId) {
        this.legId = legId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillCurrency() {
        return billCurrency;
    }

    public void setBillCurrency(String billCurrency) {
        this.billCurrency = billCurrency;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Counterparty getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(Counterparty counterparty) {
        this.counterparty = counterparty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
