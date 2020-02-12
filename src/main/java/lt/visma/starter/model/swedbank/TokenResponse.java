package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonAlias;

public class TokenResponse {
    @JsonAlias({"access_token"})
    private String accessToken;
    @JsonAlias({"expires_in"})
    private long expiresIn;
    @JsonAlias({"refresh_token"})
    private String refreshToken;
    private String scope;
    @JsonAlias({"token_type"})
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
