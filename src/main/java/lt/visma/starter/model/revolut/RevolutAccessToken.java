package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;

public class RevolutAccessToken {
    @JsonAlias({"access_token"})
    private String accessToken;
    @JsonAlias({"tokenType"})
    private String tokenType;
    @JsonAlias({"expires_in"})
    private String expiresIn;
    @JsonAlias({"refresh_token"})
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
