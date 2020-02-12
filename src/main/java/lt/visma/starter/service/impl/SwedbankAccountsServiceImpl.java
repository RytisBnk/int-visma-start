package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.swedbank.Access;
import lt.visma.starter.model.swedbank.ConsentRequest;
import lt.visma.starter.model.swedbank.ConsentResponse;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.ServerTimeService;
import lt.visma.starter.service.SwedbankAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

@Service
public class SwedbankAccountsServiceImpl implements SwedbankAccountsService {
    @Autowired
    private SwedbankConfigurationProperties configurationProperties;

    @Autowired
    private HttpRequestService httpRequestService;

    @Autowired
    private ServerTimeService serverTimeService;

    @Override
    public ConsentResponse getUserConsent(String accessToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", "SANDLT22");
        queryParams.add("app-id", configurationProperties.getClientId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Date", serverTimeService.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", "HardcodedID");
        headers.add("PSU-IP-Address", "1.1.1.1");
        headers.add("PSU-User-Agent", "User agent");

        Access access = new Access(null, "allAccounts", null, null);
        ConsentRequest requestBody = new ConsentRequest(access,
                false,0,false, "2020-04-10");

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                "/sandbox/v2/consents",
                queryParams,
                headers,
                requestBody,
                MediaType.APPLICATION_JSON
        );
        if (response == null) {
            throw new GenericException();
        }
        return response.bodyToMono(ConsentResponse.class).block();
    }
}
