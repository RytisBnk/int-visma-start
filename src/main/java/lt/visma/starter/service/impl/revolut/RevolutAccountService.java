package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.revolut.RevolutAccount;
import lt.visma.starter.model.revolut.RevolutResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.util.HTTPUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RevolutAccountService implements BankingAccountsService {
    private final RevolutConfigurationProperties configurationProperties;
    private final HttpRequestService httpRequestService;
    private final AuthenticationService revolutAuthenticationService;

    public RevolutAccountService(RevolutConfigurationProperties configurationProperties,
                                 HttpRequestService httpRequestService,
                                 AuthenticationService revolutAuthenticationService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
        this.revolutAuthenticationService = revolutAuthenticationService;
    }

    @Override
    public List<BankingAccount> getBankingAccounts(Map<String, String> parameters) throws GenericException, ApiException {
        String accessToken = revolutAuthenticationService.getAccessToken(parameters);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiURL(),
                configurationProperties.getAccountsEndpointUrl(),
                null,
                headers
        );

        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.OK) {
            RevolutAccount[] accounts = response.bodyToMono(RevolutAccount[].class).block();
            return accounts != null ? Arrays.asList(accounts) : new ArrayList<>();
        }
        else {
            RevolutResponseError apiError = response.bodyToMono(RevolutResponseError.class).block();
            throw new ApiException(apiError);
        }
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }
}
