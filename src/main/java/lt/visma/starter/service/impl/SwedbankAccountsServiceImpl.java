package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.SwedbankApiException;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.ServerTimeService;
import lt.visma.starter.service.SwedbankAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.UUID;

@Service
public class SwedbankAccountsServiceImpl implements SwedbankAccountsService {
    private SwedbankConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;
    private ServerTimeService serverTimeService;

    @Autowired
    public SwedbankAccountsServiceImpl(SwedbankConfigurationProperties configurationProperties, HttpRequestService httpRequestService, ServerTimeService serverTimeService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
        this.serverTimeService = serverTimeService;
    }

    @Override
    public ConsentResponse getUserConsent(String accessToken, String psuUserAgent, String psuIP) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Date", serverTimeService.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("PSU-IP-Address", psuIP);
        headers.add("PSU-User-Agent", psuUserAgent);

        Access access = new Access(null, "allAccounts", null, null);
        ConsentRequest requestBody = new ConsentRequest(access,
                false,0,false, "2020-04-10");

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getConsentsEndpointUrl(),
                queryParams,
                headers,
                requestBody,
                MediaType.APPLICATION_JSON
        );
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.BAD_REQUEST) {
            throw new SwedbankApiException(response.bodyToMono(ResponseError.class).block());
        }
        return response.bodyToMono(ConsentResponse.class).block();
    }

    @Override
    public AccountsListResponse getUserAccounts(String consentId, String accessToken, String psuUserAgent, String psuIP, String psuID) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("PSU-ID", psuID);
        headers.add("Date", serverTimeService.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("Consent-ID", consentId);
        headers.add("PSU-User-Agent", psuUserAgent);
        headers.add("PSU-IP-Address", psuIP);
        headers.add("Authorization", "Bearer " + accessToken);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getAccountsEndpointUrl(),
                queryParams,
                headers
        );
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.BAD_REQUEST) {
            ResponseError responseError = response.bodyToMono(ResponseError.class).block();
            throw new SwedbankApiException(responseError);
        }
        return response.bodyToMono(AccountsListResponse.class).block();
    }
}
