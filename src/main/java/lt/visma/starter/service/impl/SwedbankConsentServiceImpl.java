package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.Access;
import lt.visma.starter.model.swedbank.ConsentRequest;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
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

import java.util.Map;
import java.util.UUID;

@Service
public class SwedbankConsentServiceImpl implements ConsentService {
    private HttpRequestService httpRequestService;
    private SwedbankConfigurationProperties configurationProperties;

    @Autowired
    public SwedbankConsentServiceImpl(HttpRequestService httpRequestService, SwedbankConfigurationProperties configurationProperties) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public ConsentResponse createUserConsent(String accessToken, Map<String, String> params) throws GenericException, ApiException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", configurationProperties.getBic());
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("PSU-IP-Address", params.get("host"));
        headers.add("PSU-User-Agent", params.get("user-agent"));

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
            throw new ApiException(response.bodyToMono(SwedbankResponseError.class).block());
        }
        return response.bodyToMono(ConsentResponse.class).block();
    }
}
