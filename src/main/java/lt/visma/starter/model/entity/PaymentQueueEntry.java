package lt.visma.starter.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentQueueEntry {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String paymentId;
    private QueueEntryState status;
    @JsonIgnore
    private String bankCode;
    @OneToOne(cascade = CascadeType.ALL)
    private PaymentProcessingError error;

    public PaymentProcessingError getError() {
        return error;
    }

    public void setError(PaymentProcessingError error) {
        this.error = error;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public QueueEntryState getStatus() {
        return status;
    }

    public void setStatus(QueueEntryState status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
