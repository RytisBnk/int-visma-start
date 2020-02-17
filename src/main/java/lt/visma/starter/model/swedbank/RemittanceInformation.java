package lt.visma.starter.model.swedbank;

public class RemittanceInformation {
    private String reference;
    private String referenceType;
    private String referenceTypeIssuer;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReferenceTypeIssuer() {
        return referenceTypeIssuer;
    }

    public void setReferenceTypeIssuer(String referenceTypeIssuer) {
        this.referenceTypeIssuer = referenceTypeIssuer;
    }
}
