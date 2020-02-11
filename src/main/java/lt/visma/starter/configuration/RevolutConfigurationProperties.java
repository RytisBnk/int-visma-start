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
