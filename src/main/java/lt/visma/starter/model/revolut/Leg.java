package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Leg {
    @JsonAlias({"leg_id"})
    private String ledId;
    private double amount;
    private String currency;
    @JsonAlias({"bill_amount"})
    private double billAmount;
    @JsonAlias({"bill_currency"})
    private String billCurrency;
    @JsonAlias({"account_id"})
    private String accountId;
    private Counterparty counterparty;
    private String description;
    private double balance;

}
