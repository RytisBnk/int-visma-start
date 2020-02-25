package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class SwedbankAuthenticationService implements AuthenticationService {
    private final SwedbankConfigurationProperties configurationProperties;
    private final HttpRequestService httpRequestService;

    @Autowired
    public SwedbankAuthenticationService(SwedbankConfigurationProperties configurationProperties, HttpRequestService httpRequestService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public String getAccessToken(Map<String, String> parameters) throws GenericException, ApiException {
        DecoupledAuthResponse authResponse = getAuthorizationID(parameters.get("psu-id"), parameters.get("sca-method"));
        AuthorizationCodeResponse authorizationCodeResponse =
                getAuthorisationCode(authResponse.getAuthorizeId(), parameters.get("psu-id"));

        MultiValueMap<String, String> queryParams =
                getAccessTokenRequestQueryParams(authorizationCodeResponse.getAuthorizationCode())
;
        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getTokenEndpointUrl(),
                queryParams,
                new LinkedMultiValueMap<>(),
                null,
                MediaType.APPLICATION_FORM_URLENCODED
        );

        checkIfResponseValid(response);
        return Objects.requireNonNull(response.bodyToMono(TokenResponse.class).block()).getAccessToken();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private DecoupledAuthResponse getAuthorizationID(String psuID, String scaMethod)
            throws GenericException, ApiException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());

        MultiValueMap<String, String> headers = getStandardHeaders(psuID);

        DecoupledAuthRequest requestBody = new DecoupledAuthRequest(
                scaMethod,
                configurationProperties.getClientId(),
                new PSUData(configurationProperties.getBic(), psuID, null),
                configurationProperties.getRedirectUrl(),
                AccessScope.PSD2sandbox
        );

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getAuthenticationEndpointUrl(),
                queryParams,
                headers,
                requestBody,
                MediaType.APPLICATION_JSON
        );

        checkIfResponseValid(response);
        return response.bodyToMono(DecoupledAuthResponse.class).block();
    }

    private AuthorizationCodeResponse getAuthorisationCode(String authoriseId, String psuID)
            throws GenericException, ApiException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("client_id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = getStandardHeaders(psuID);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getAuthenticationEndpointUrl() + "/authorize/" + authoriseId,
                queryParams,
                headers
        );

        checkIfResponseValid(response);
        return response.bodyToMono(AuthorizationCodeResponse.class).block();
    }

    private MultiValueMap<String, String> getAccessTokenRequestQueryParams(String authorisationCode) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("client_id", configurationProperties.getClientId());
        queryParams.add("client_secret", configurationProperties.getClientSecret());
        queryParams.add("grant_type", "authorization_code");
        queryParams.add("code", authorisationCode);
        queryParams.add("redirect_uri", configurationProperties.getRedirectUrl());

        return queryParams;
    }

    private MultiValueMap<String, String> getStandardHeaders(String psuID) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("PSU-ID", psuID);

        return headers;
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() != HttpStatus.OK) {
            throw new ApiException(response.bodyToMono(SwedbankResponseError.class).block());
        }
    }
}
