package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.swedbank.SwedbankPaymentRequest;
import lt.visma.starter.model.swedbank.SwedbankPaymentResponse;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.PaymentService;
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
public class SwedbankPaymentService implements PaymentService {
    private HttpRequestService httpRequestService;
    private SwedbankConfigurationProperties configurationProperties;
    private AuthenticationService swedbankAuthenticationService;

    @Autowired
    public SwedbankPaymentService(HttpRequestService httpRequestService,
                                  SwedbankConfigurationProperties configurationProperties,
                                  AuthenticationService swedbankAuthenticationService) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.swedbankAuthenticationService = swedbankAuthenticationService;
    }

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest, Map<String, String> params) throws GenericException, ApiException {
        String accessToken = swedbankAuthenticationService.getAccessToken(params);

        if (! (paymentRequest instanceof SwedbankPaymentRequest)) {
            throw new GenericException();
        }
        SwedbankPaymentRequest swedbankPaymentRequest = (SwedbankPaymentRequest) paymentRequest;

        MultiValueMap<String, String> queryparams = new LinkedMultiValueMap<>();
        queryparams.add("bic", swedbankPaymentRequest.getBic());

        MultiValueMap<String, String> headers = getRequiredHeaders(accessToken, swedbankPaymentRequest);

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getPaymentsEndpointUrl(),
                queryparams,
                headers,
                swedbankPaymentRequest,
                MediaType.APPLICATION_JSON
        );
        checkIfResponseValid(response);
        return response.bodyToMono(SwedbankPaymentResponse.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private MultiValueMap<String, String> getRequiredHeaders(String accessToken, SwedbankPaymentRequest paymentRequest) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);
        headers.add("PSU-IP-Address", paymentRequest.getPsuIPAddress());
        headers.add("PSU-User-Agent", paymentRequest.getPsuUserAgent());
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("Tpp-Redirect-URI", configurationProperties.getRedirectUrl());

        return headers;
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() != HttpStatus.CREATED) {
            SwedbankResponseError swedbankResponseError = response.bodyToMono(SwedbankResponseError.class).block();
            throw new ApiException(swedbankResponseError);
        }
    }
}
