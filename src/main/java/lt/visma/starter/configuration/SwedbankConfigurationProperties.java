package lt.visma.starter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "swedbank")
public class SwedbankConfigurationProperties {
    private String apiUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String bic;
    private String consentsEndpointUrl;
    private String accountsEndpointUrl;
    private String authenticationEndpointUrl;
    private String tokenEndpointUrl;
    private String[] supportedBanks;
    private String paymentsEndpointUrl;
    private String paymentDetailsEndpointUrl;

    public String getPaymentDetailsEndpointUrl() {
        return paymentDetailsEndpointUrl;
    }

    public void setPaymentDetailsEndpointUrl(String paymentDetailsEndpointUrl) {
        this.paymentDetailsEndpointUrl = paymentDetailsEndpointUrl;
    }

    public String getPaymentsEndpointUrl() {
        return paymentsEndpointUrl;
    }

    public void setPaymentsEndpointUrl(String paymentsEndpointUrl) {
        this.paymentsEndpointUrl = paymentsEndpointUrl;
    }

    public String[] getSupportedBanks() {
        return supportedBanks;
    }

    public void setSupportedBanks(String[] supportedBanks) {
        this.supportedBanks = supportedBanks;
    }

    public String getConsentsEndpointUrl() {
        return consentsEndpointUrl;
    }

    public void setConsentsEndpointUrl(String consentsEndpointUrl) {
        this.consentsEndpointUrl = consentsEndpointUrl;
    }

    public String getAccountsEndpointUrl() {
        return accountsEndpointUrl;
    }

    public void setAccountsEndpointUrl(String accountsEndpointUrl) {
        this.accountsEndpointUrl = accountsEndpointUrl;
    }

    public String getAuthenticationEndpointUrl() {
        return authenticationEndpointUrl;
    }

    public void setAuthenticationEndpointUrl(String authenticationEndpointUrl) {
        this.authenticationEndpointUrl = authenticationEndpointUrl;
    }

    public String getTokenEndpointUrl() {
        return tokenEndpointUrl;
    }

    public void setTokenEndpointUrl(String tokenEndpointUrl) {
        this.tokenEndpointUrl = tokenEndpointUrl;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
