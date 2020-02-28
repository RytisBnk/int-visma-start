package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemittanceInformation implements Serializable {
    private String reference;
    private String referenceType;

    public RemittanceInformation() {
    }

    public RemittanceInformation(String reference) {
        this.reference = reference;
    }

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
}
