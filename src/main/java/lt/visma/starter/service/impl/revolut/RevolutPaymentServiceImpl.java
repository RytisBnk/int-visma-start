package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.revolut.RevolutApiError;
import lt.visma.starter.model.revolut.RevolutPaymentResponse;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.util.HTTPUtils;
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
public class RevolutPaymentServiceImpl implements PaymentService {
    private HttpRequestService httpRequestService;
    private RevolutConfigurationProperties configurationProperties;
    private AuthenticationService revolutAuthenticationService;

    public RevolutPaymentServiceImpl(HttpRequestService httpRequestService,
                                     RevolutConfigurationProperties configurationProperties,
                                     AuthenticationService revolutAuthenticationService) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.revolutAuthenticationService = revolutAuthenticationService;
    }

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest, Map<String, String> params) throws GenericException, ApiException {
        String accessToken = revolutAuthenticationService.getAccessToken(params);

        if (! (paymentRequest instanceof RevolutPaymentRequest)) {
            throw new GenericException();
        }
        RevolutPaymentRequest revolutPaymentRequest = (RevolutPaymentRequest) paymentRequest;
        revolutPaymentRequest.setRequestId(UUID.randomUUID().toString());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiURL(),
                configurationProperties.getPaymentsEndpointUrl(),
                null,
                headers,
                revolutPaymentRequest,
                MediaType.APPLICATION_JSON
        );

        checkIfResponseValid(response);
        return response.bodyToMono(RevolutPaymentResponse.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.BAD_REQUEST) {
            RevolutApiError revolutApiError = response.bodyToMono(RevolutApiError.class).block();
            throw new ApiException(revolutApiError);
        }
    }
}
