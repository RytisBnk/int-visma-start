package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.util.HTTPUtils;
import lt.visma.starter.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.*;

@Service
public class SwedbankAccountService implements BankingAccountsService {
    private SwedbankConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;
    private AuthenticationService swedbankAuthenticationService;

    @Autowired
    public SwedbankAccountService(SwedbankConfigurationProperties configurationProperties,
                                  HttpRequestService httpRequestService,
                                  AuthenticationService swedbankAuthenticationService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
        this.swedbankAuthenticationService = swedbankAuthenticationService;
    }

    @Override
    public List<BankingAccount> getBankingAccounts(Map<String, String> parameters)
            throws GenericException, ApiException {
        String accessToken = swedbankAuthenticationService.getAccessToken(parameters);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = getRequiredHeaders(accessToken,parameters);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getAccountsEndpointUrl(),
                queryParams,
                headers
        );

        checkIfResponseValid(response);
        AccountsListResponse accountsListResponse = response.bodyToMono(AccountsListResponse.class).block();
        if (accountsListResponse == null) {
            throw new GenericException();
        }
        List<? extends BankingAccount> accounts = accountsListResponse.getAccounts();
        return (List<BankingAccount>) accounts;
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private MultiValueMap<String, String> getRequiredHeaders(String accessToken, Map<String, String> parameters) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("PSU-ID", parameters.get("psu-id"));
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("Consent-ID", parameters.get("consent-id"));
        headers.add("PSU-User-Agent", parameters.get("user-agent"));
        headers.add("PSU-IP-Address", parameters.get("host"));
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        return headers;
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.BAD_REQUEST) {
            SwedbankResponseError swedbankResponseError = response.bodyToMono(SwedbankResponseError.class).block();
            throw new ApiException(swedbankResponseError);
        }
    }
}
