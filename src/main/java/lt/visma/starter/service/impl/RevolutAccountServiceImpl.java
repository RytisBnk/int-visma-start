package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.RevolutApiException;
import lt.visma.starter.model.revolut.RevolutAccessToken;
import lt.visma.starter.model.revolut.RevolutAccount;
import lt.visma.starter.model.revolut.ResponseError;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.RevolutAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RevolutAccountServiceImpl implements RevolutAccountsService {
    private RevolutConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;

    @Autowired
    public RevolutAccountServiceImpl(RevolutConfigurationProperties configurationProperties, HttpRequestService httpRequestService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public List<RevolutAccount> getAccounts(RevolutAccessToken accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken.getAccessToken());

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
            ResponseError apiError = response.bodyToMono(ResponseError.class).block();
            throw new RevolutApiException(apiError);
        }
    }
}
