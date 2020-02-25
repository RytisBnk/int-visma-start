package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.mapper.PaymentSubmissionMapper;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.entity.PaymentSubmission;
import lt.visma.starter.model.revolut.RevolutApiError;
import lt.visma.starter.model.revolut.RevolutPaymentResponse;
import lt.visma.starter.model.revolut.RevolutPaymentRequest;
import lt.visma.starter.repository.PaymentSubmissionRepository;
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
import java.util.UUID;

@Service
public class RevolutPaymentService implements PaymentService {
    private final HttpRequestService httpRequestService;
    private final RevolutConfigurationProperties configurationProperties;
    private final PaymentSubmissionRepository paymentSubmissionRepository;
    private final PaymentSubmissionMapper revolutPaymentSubmissionMapper;

    public RevolutPaymentService(HttpRequestService httpRequestService,
                                 RevolutConfigurationProperties configurationProperties,
                                 PaymentSubmissionMapper revolutPaymentSubmissionMapper,
                                 PaymentSubmissionRepository paymentSubmissionRepository) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.revolutPaymentSubmissionMapper = revolutPaymentSubmissionMapper;
        this.paymentSubmissionRepository = paymentSubmissionRepository;
    }

    @Override
    public PaymentSubmission makePayment(PaymentRequest paymentRequest, String accessToken)
            throws GenericException, ApiException, InvalidPaymentResponseException {
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
        PaymentResponse paymentResponse = response.bodyToMono(RevolutPaymentResponse.class).block();
        return paymentSubmissionRepository.save(revolutPaymentSubmissionMapper.mapToPaymentSubmission(paymentResponse));
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
