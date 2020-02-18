package lt.visma.starter.model.swedbank;

import lt.visma.starter.model.Transaction;

public class SwedbankPaymentTransaction implements Transaction {
    private AccountIBAN creditorAccount;
    private String creditorAgent;
    private String creditorName;
    private AccountIBAN debtorAccount;
    private String debtorIdentification;
    private String endToEndIdentification;
    private PaymentAmount instructedAmount;
    private String remittanceInformationUnstructured;

    public AccountIBAN getCreditorAccount() {
        return creditorAccount;
    }

    public void setCreditorAccount(AccountIBAN creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public String getCreditorAgent() {
        return creditorAgent;
    }

    public void setCreditorAgent(String creditorAgent) {
        this.creditorAgent = creditorAgent;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public AccountIBAN getDebtorAccount() {
        return debtorAccount;
    }

    public void setDebtorAccount(AccountIBAN debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public String getDebtorIdentification() {
        return debtorIdentification;
    }

    public void setDebtorIdentification(String debtorIdentification) {
        this.debtorIdentification = debtorIdentification;
    }

    public String getEndToEndIdentification() {
        return endToEndIdentification;
    }

    public void setEndToEndIdentification(String endToEndIdentification) {
        this.endToEndIdentification = endToEndIdentification;
    }

    public PaymentAmount getInstructedAmount() {
        return instructedAmount;
    }

    public void setInstructedAmount(PaymentAmount instructedAmount) {
        this.instructedAmount = instructedAmount;
    }

    public String getRemittanceInformationUnstructured() {
        return remittanceInformationUnstructured;
    }

    public void setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
        this.remittanceInformationUnstructured = remittanceInformationUnstructured;
    }
}
