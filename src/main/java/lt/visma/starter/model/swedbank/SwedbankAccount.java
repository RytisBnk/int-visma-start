package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lt.visma.starter.model.BankingAccount;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwedbankAccount implements BankingAccount {
    private String cashAccountType;
    private String currency;
    private String iban;
    private String name;
    private String product;
    private String resourceId;

    @JsonAlias({"_links"})
    private AccountLinks accountLinks;

    public SwedbankAccount(String cashAccountType, String currency, String iban, String name, String product, String resourceId) {
        this.cashAccountType = cashAccountType;
        this.currency = currency;
        this.iban = iban;
        this.name = name;
        this.product = product;
        this.resourceId = resourceId;
    }

    public SwedbankAccount() {
    }

    public AccountLinks getAccountLinks() {
        return accountLinks;
    }

    public void setAccountLinks(AccountLinks accountLinks) {
        this.accountLinks = accountLinks;
    }

    public String getCashAccountType() {
        return cashAccountType;
    }

    public void setCashAccountType(String cashAccountType) {
        this.cashAccountType = cashAccountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
