package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.RevolutApiException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.revolut.ResponseError;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Arrays;

@Service
public class RevolutPaymentServiceImpl implements PaymentService {
    private HttpRequestService httpRequestService;
    private RevolutConfigurationProperties configurationProperties;

    private String[] supportedBanks = new String[] {"REVOGB21"};
    private String[] requiredParams = new String[] {
            "requestID", "accountID", "counterpartyID", "receiverID", "receiverAccountID",
            "amount", "currency", "reference"
    };

    public RevolutPaymentServiceImpl(HttpRequestService httpRequestService, RevolutConfigurationProperties configurationProperties) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public String makePayment(String accessToken, PaymentRequest paymentRequest) throws GenericException, ApiException {
        if (! (paymentRequest instanceof RevolutPaymentRequest)) {
            throw new GenericException();
        }
        RevolutPaymentRequest revolutPaymentRequest = (RevolutPaymentRequest) paymentRequest;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accessToken);

        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiURL(),
                configurationProperties.getPaymentsEndpointUrl(),
                null,
                headers,
                revolutPaymentRequest,
                MediaType.APPLICATION_JSON
        );

//        checkIfResponseValid(response);
        return response.bodyToMono(String.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(supportedBanks).contains(bankCode);
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() != HttpStatus.CREATED) {
            ResponseError responseError = response.bodyToMono(ResponseError.class).block();
            throw new RevolutApiException(responseError);
        }
    }
}
