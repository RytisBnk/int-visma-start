package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DecoupledAuthResponse {
    private String authorizeId;
    private String chosenScaMethod;
    private ChallengeData challengeData;
    private String psuMessage;
    @JsonAlias({"_links"})
    private DecoupledAuthResponseLinks links;

    public String getAuthorizeId() {
        return authorizeId;
    }

    public void setAuthorizeId(String authorizeId) {
        this.authorizeId = authorizeId;
    }

    public String getChosenScaMethod() {
        return chosenScaMethod;
    }

    public void setChosenScaMethod(String chosenScaMethod) {
        this.chosenScaMethod = chosenScaMethod;
    }

    public ChallengeData getChallengeData() {
        return challengeData;
    }

    public void setChallengeData(ChallengeData challengeData) {
        this.challengeData = challengeData;
    }

    public String getPsuMessage() {
        return psuMessage;
    }

    public void setPsuMessage(String psuMessage) {
        this.psuMessage = psuMessage;
    }

    public DecoupledAuthResponseLinks getLinks() {
        return links;
    }

    public void setLinks(DecoupledAuthResponseLinks links) {
        this.links = links;
    }
}
