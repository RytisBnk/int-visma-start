package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.mapper.PaymentSubmissionMapper;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.dto.PaymentQueueMessage;
import lt.visma.starter.model.entity.PaymentSubmission;
import lt.visma.starter.model.revolut.RevolutApiError;
import lt.visma.starter.model.revolut.RevolutPaymentResponse;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.repository.PaymentSubmissionRepository;
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
public class RevolutPaymentService implements PaymentService {
    private final HttpRequestService httpRequestService;
    private final RevolutConfigurationProperties configurationProperties;
    private final PaymentSubmissionRepository paymentSubmissionRepository;
    private final PaymentSubmissionMapper revolutPaymentSubmissionMapper;
    private final AuthenticationService revolutAuthenticationService;

    public RevolutPaymentService(HttpRequestService httpRequestService,
                                 RevolutConfigurationProperties configurationProperties,
                                 PaymentSubmissionMapper revolutPaymentSubmissionMapper,
                                 PaymentSubmissionRepository paymentSubmissionRepository,
                                 AuthenticationService revolutAuthenticationService) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.revolutPaymentSubmissionMapper = revolutPaymentSubmissionMapper;
        this.paymentSubmissionRepository = paymentSubmissionRepository;
        this.revolutAuthenticationService = revolutAuthenticationService;
    }

    @Override
    public void makePayment(PaymentQueueMessage paymentQueueMessage)
            throws GenericException, ApiException, InvalidPaymentResponseException {
        PaymentRequest paymentRequest = paymentQueueMessage.getPaymentRequest();
        if (! (paymentRequest instanceof RevolutPaymentRequest)) {
            throw new GenericException();
        }
        RevolutPaymentRequest revolutPaymentRequest = (RevolutPaymentRequest) paymentRequest;
        revolutPaymentRequest.setRequestId(UUID.randomUUID().toString());

        String accessToken = revolutAuthenticationService.getAccessToken(paymentQueueMessage.getAuthParams());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        PaymentResponse paymentResponse = getPaymentResponseFromAPI(headers, revolutPaymentRequest);
        PaymentSubmission paymentSubmission =
                paymentSubmissionRepository.save(revolutPaymentSubmissionMapper.mapToPaymentSubmission(paymentResponse));
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private PaymentResponse getPaymentResponseFromAPI(MultiValueMap<String, String> headers,
                                                      RevolutPaymentRequest revolutPaymentRequest)
            throws GenericException, ApiException {
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
