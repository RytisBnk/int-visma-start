package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.*;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.ServerTimeService;
import lt.visma.starter.service.SwedBankAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

@Service
public class SwedbankAuthServiceImpl implements SwedBankAuthenticationService {
    private SwedbankConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;
    private ServerTimeService serverTimeService;

    @Autowired
    public SwedbankAuthServiceImpl(SwedbankConfigurationProperties configurationProperties, HttpRequestService httpRequestService, ServerTimeService serverTimeService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
        this.serverTimeService = serverTimeService;
    }

    @Override
    public TokenResponse getAccessToken() {
        DecoupledAuthResponse authResponse = getAuthorizationID();
        AuthorizationCodeResponse authorizationCodeResponse =
                getAuthorisationCode(authResponse.getAuthorizeId());

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("client_id", configurationProperties.getClientId());
        queryParams.add("client_secret", configurationProperties.getClientSecret());
        queryParams.add("grant_type", "authorization_code");
        queryParams.add("code", authorizationCodeResponse.getAuthorizationCode());
        queryParams.add("redirect_uri", configurationProperties.getRedirectUrl());

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                "/psd2/token",
                queryParams,
                new LinkedMultiValueMap<>(),
                null,
                MediaType.APPLICATION_FORM_URLENCODED
        );
        if (response == null) {
            throw new GenericException();
        }
        return response.bodyToMono(TokenResponse.class).block();
    }

    private DecoupledAuthResponse getAuthorizationID() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", "SANDLT22");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Date", serverTimeService.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", "HardcodedRequestID1");
        headers.add("PSU-ID", "HardcodedID1");

        DecoupledAuthRequest requestBody = new DecoupledAuthRequest(
                "SMART_ID",
                configurationProperties.getClientId(),
                new PSUData("SANDLT22", "HardcodedID1", null),
                configurationProperties.getRedirectUrl(),
                AccessScope.PSD2sandbox
        );

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                "/psd2/authorize-decoupled",
                queryParams,
                headers,
                requestBody,
                MediaType.APPLICATION_JSON
        );

        if (response == null) {
            throw new GenericException();
        }
        return response.bodyToMono(DecoupledAuthResponse.class).block();
    }

    private AuthorizationCodeResponse getAuthorisationCode(String authoriseId) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", "SANDLT22");
        queryParams.add("client_id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Date", serverTimeService.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", "HardcodedRequestID1");
        headers.add("PSU-ID", "HardcodedID1");

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiUrl(),
                "/psd2/authorize-decoupled/authorize/" + authoriseId,
                queryParams,
                headers
        );

        if (response == null) {
            throw new GenericException();
        }
        return response.bodyToMono(AuthorizationCodeResponse.class).block();
    }
}
