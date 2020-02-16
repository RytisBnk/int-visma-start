package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DecoupledAuthRequest {
    private String chosenScaMethod;
    private String clientID;
    private PSUData psuData;
    private String redirectUri;
    private AccessScope scope;

    public DecoupledAuthRequest(String chosenScaMethod, String clientID, PSUData psuData, String redirectUri, AccessScope scope) {
        this.chosenScaMethod = chosenScaMethod;
        this.clientID = clientID;
        this.psuData = psuData;
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    public DecoupledAuthRequest() {
    }

    public String getChosenScaMethod() {
        return chosenScaMethod;
    }

    public void setChosenScaMethod(String chosenScaMethod) {
        this.chosenScaMethod = chosenScaMethod;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public PSUData getPsuData() {
        return psuData;
    }

    public void setPsuData(PSUData psuData) {
        this.psuData = psuData;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public AccessScope getScope() {
        return scope;
    }

    public void setScope(AccessScope scope) {
        this.scope = scope;
    }
}
