package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsentResponse {
    @JsonAlias({"_links"})
    private ConsentResponseLinks links;
    private String chosenScaMethod;
    private String consentId;
    private String consentStatus;
    private String scaMethods;

    public ConsentResponseLinks getLinks() {
        return links;
    }

    public void setLinks(ConsentResponseLinks links) {
        this.links = links;
    }

    public String getChosenScaMethod() {
        return chosenScaMethod;
    }

    public void setChosenScaMethod(String chosenScaMethod) {
        this.chosenScaMethod = chosenScaMethod;
    }

    public String getConsentId() {
        return consentId;
    }

    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    public String getConsentStatus() {
        return consentStatus;
    }

    public void setConsentStatus(String consentStatus) {
        this.consentStatus = consentStatus;
    }

    public String getScaMethods() {
        return scaMethods;
    }

    public void setScaMethods(String scaMethods) {
        this.scaMethods = scaMethods;
    }
}
