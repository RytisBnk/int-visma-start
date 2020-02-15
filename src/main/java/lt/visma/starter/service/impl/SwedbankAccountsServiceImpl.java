package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.SwedbankApiException;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.SwedbankAccountsService;
import lt.visma.starter.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.UUID;

@Service
public class SwedbankAccountsServiceImpl implements SwedbankAccountsService {
    private SwedbankConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;

    @Autowired
    public SwedbankAccountsServiceImpl(SwedbankConfigurationProperties configurationProperties, HttpRequestService httpRequestService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public AccountsListResponse getUserAccounts(String consentId, String accessToken, String psuUserAgent, String psuIP, String psuID)
            throws GenericException, ApiException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = getRequiredHeaders(accessToken,consentId,psuID,psuUserAgent,psuIP);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getAccountsEndpointUrl(),
                queryParams,
                headers
        );

        checkIfResponseValid(response);

        return response.bodyToMono(AccountsListResponse.class).block();
    }

    private MultiValueMap<String, String> getRequiredHeaders(String accessToken, String consentId, String psuID, String psuUserAgent, String psuIP) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("PSU-ID", psuID);
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("Consent-ID", consentId);
        headers.add("PSU-User-Agent", psuUserAgent);
        headers.add("PSU-IP-Address", psuIP);
        headers.add("Authorization", "Bearer " + accessToken);

        return headers;
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.BAD_REQUEST) {
            ResponseError responseError = response.bodyToMono(ResponseError.class).block();
            throw new SwedbankApiException(responseError);
        }
    }
}
