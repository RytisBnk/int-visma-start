package lt.visma.starter.model.swedbank.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.visma.starter.model.Transaction;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class SwedbankPaymentTransaction implements Transaction {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private AccountIBAN creditorAccount;
    private String creditorAgent;
    private String creditorName;
    @ManyToOne(cascade = CascadeType.MERGE)
    private AccountIBAN debtorAccount;
    private String debtorIdentification;
    private String endToEndIdentification;
    @OneToOne(cascade = CascadeType.ALL)
    private PaymentAmount instructedAmount;
    private String remittanceInformationUnstructured;
    @OneToOne(cascade = CascadeType.ALL)
    private RemittanceInformation remittanceInformationStructured;

    public RemittanceInformation getRemittanceInformationStructured() {
        return remittanceInformationStructured;
    }

    public void setRemittanceInformationStructured(RemittanceInformation remittanceInformationStructured) {
        this.remittanceInformationStructured = remittanceInformationStructured;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
