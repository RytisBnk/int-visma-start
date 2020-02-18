package lt.visma.starter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "revolut")
public class RevolutConfigurationProperties {
    private String clientId;
    private String authorisationCode;
    private String privateKeyFilepath;
    private String apiURL;
    private String refreshToken;
    private String iss;
    private String aud;
    private String clientAssertionType;
    private String authenticationEndpointUrl;
    private String accountsEndpointUrl;
    private String paymentsEndpointUrl;
    private String[] supportedBanks;

    public String[] getSupportedBanks() {
        return supportedBanks;
    }

    public void setSupportedBanks(String[] supportedBanks) {
        this.supportedBanks = supportedBanks;
    }

    public String getPaymentsEndpointUrl() {
        return paymentsEndpointUrl;
    }

    public void setPaymentsEndpointUrl(String paymentsEndpointUrl) {
        this.paymentsEndpointUrl = paymentsEndpointUrl;
    }

    public String getAuthenticationEndpointUrl() {
        return authenticationEndpointUrl;
    }

    public void setAuthenticationEndpointUrl(String authenticationEndpointUrl) {
        this.authenticationEndpointUrl = authenticationEndpointUrl;
    }

    public String getAccountsEndpointUrl() {
        return accountsEndpointUrl;
    }

    public void setAccountsEndpointUrl(String accountsEndpointUrl) {
        this.accountsEndpointUrl = accountsEndpointUrl;
    }

    public String getClientAssertionType() {
        return clientAssertionType;
    }

    public void setClientAssertionType(String clientAssertionType) {
        this.clientAssertionType = clientAssertionType;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthorisationCode() {
        return authorisationCode;
    }

    public void setAuthorisationCode(String authorisationCode) {
        this.authorisationCode = authorisationCode;
    }

    public String getPrivateKeyFilepath() {
        return privateKeyFilepath;
    }

    public void setPrivateKeyFilepath(String privateKeyFilepath) {
        this.privateKeyFilepath = privateKeyFilepath;
    }
}
