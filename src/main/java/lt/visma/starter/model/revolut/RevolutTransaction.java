package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;
import lt.visma.starter.model.Transaction;

import java.util.List;

public class RevolutTransaction implements Transaction {
    private String id;
    private RevolutTransactionType type;
    @JsonAlias({"request_id"})
    private String reuestId;
    private PaymentState state;
    @JsonAlias({"reason_code"})
    private String reasonCode;
    @JsonAlias({"created_at"})
    private String createdAt;
    @JsonAlias({"updated_at"})
    private String updatedAt;
    @JsonAlias({"completed_at"})
    private String completedAt;
    @JsonAlias({"scheduled_for"})
    private String scheduledFor;
    @JsonAlias({"related_transaction_id"})
    private String relatedTransactionId;
    private Merchant merchant;
    private List<TransactionLeg> legs;
    private Card card;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RevolutTransactionType getType() {
        return type;
    }

    public void setType(RevolutTransactionType type) {
        this.type = type;
    }

    public String getReuestId() {
        return reuestId;
    }

    public void setReuestId(String reuestId) {
        this.reuestId = reuestId;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
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

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(String scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public String getRelatedTransactionId() {
        return relatedTransactionId;
    }

    public void setRelatedTransactionId(String relatedTransactionId) {
        this.relatedTransactionId = relatedTransactionId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<TransactionLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<TransactionLeg> legs) {
        this.legs = legs;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
