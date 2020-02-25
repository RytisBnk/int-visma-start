package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.Access;
import lt.visma.starter.model.swedbank.ConsentRequest;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.ConsentService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.util.HTTPUtils;
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
import java.util.UUID;

@Service
public class SwedbankConsentService implements ConsentService {
    private HttpRequestService httpRequestService;
    private SwedbankConfigurationProperties configurationProperties;
    private AuthenticationService swedbankAuthenticationService;

    @Autowired
    public SwedbankConsentService(HttpRequestService httpRequestService,
                                  SwedbankConfigurationProperties configurationProperties,
                                  AuthenticationService swedbankAuthenticationService) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.swedbankAuthenticationService = swedbankAuthenticationService;
    }

    @Override
    public ConsentResponse createUserConsent(Map<String, String> params) throws GenericException, ApiException {
        String accessToken = swedbankAuthenticationService.getAccessToken(params);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = getRequiredHeaders(accessToken, params);

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
        checkIfResponseValid(response);
        return response.bodyToMono(ConsentResponse.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private MultiValueMap<String, String> getRequiredHeaders(String accessToken, Map<String, String> params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("PSU-IP-Address", params.get("host"));
        headers.add("PSU-User-Agent", params.get("user-agent"));

        return headers;
    }

    private void checkIfResponseValid(ClientResponse response) throws ApiException, GenericException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.BAD_REQUEST) {
            throw new ApiException(response.bodyToMono(SwedbankResponseError.class).block());
        }
    }
}
