package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.revolut.RevolutAccount;
import lt.visma.starter.model.revolut.RevolutResponseError;
import lt.visma.starter.service.BankingAccountsService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.util.HTTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RevolutAccountServiceImpl implements BankingAccountsService {
    private RevolutConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;

    private String[] supportedBanks = new String[] {"REVOGB21"};

    @Autowired
    public RevolutAccountServiceImpl(RevolutConfigurationProperties configurationProperties, HttpRequestService httpRequestService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public List<BankingAccount> getBankingAccounts(String accessToken, Map<String, String> parameters) throws GenericException, ApiException {
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
        return Arrays.asList(supportedBanks).contains(bankCode);
    }
}
