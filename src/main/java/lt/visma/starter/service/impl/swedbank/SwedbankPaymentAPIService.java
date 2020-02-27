package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidPaymentResponseException;
import lt.visma.starter.jms.TransactionQueueMessageSender;
import lt.visma.starter.mapper.PaymentSubmissionMapper;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.PaymentResponse;
import lt.visma.starter.model.dto.PaymentQueueMessage;
import lt.visma.starter.model.entity.PaymentSubmission;
import lt.visma.starter.model.swedbank.SwedbankPaymentRequest;
import lt.visma.starter.model.swedbank.SwedbankPaymentResponse;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import lt.visma.starter.repository.PaymentSubmissionRepository;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.PaymentAPIService;
import lt.visma.starter.util.HTTPUtils;
import lt.visma.starter.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SwedbankPaymentAPIService implements PaymentAPIService {
    private final HttpRequestService httpRequestService;
    private final SwedbankConfigurationProperties configurationProperties;
    private final PaymentSubmissionRepository paymentSubmissionRepository;
    private final PaymentSubmissionMapper swedbankPaymentSubmissionMapper;
    private final AuthenticationService swedbankAuthenticationService;
    private final TransactionQueueMessageSender transactionQueueMessageSender;

    private final Logger LOGGER = LoggerFactory.getLogger("SwedbankPaymentService");

    public SwedbankPaymentAPIService(HttpRequestService httpRequestService,
                                     SwedbankConfigurationProperties configurationProperties,
                                     PaymentSubmissionRepository paymentSubmissionRepository,
                                     PaymentSubmissionMapper swedbankPaymentSubmissionMapper,
                                     AuthenticationService swedbankAuthenticationService,
                                     TransactionQueueMessageSender transactionQueueMessageSender) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.paymentSubmissionRepository = paymentSubmissionRepository;
        this.swedbankPaymentSubmissionMapper = swedbankPaymentSubmissionMapper;
        this.swedbankAuthenticationService = swedbankAuthenticationService;
        this.transactionQueueMessageSender = transactionQueueMessageSender;
    }

    @Override
    public void makePayment(PaymentQueueMessage paymentQueueMessage, String queueEntryId) throws GenericException, ApiException, InvalidPaymentResponseException {
        LOGGER.info("Called SwedbankPaymentService.makePayment");

        PaymentRequest paymentRequest = paymentQueueMessage.getPaymentRequest();
        if (! (paymentRequest instanceof SwedbankPaymentRequest)) {
            throw new GenericException();
        }
        SwedbankPaymentRequest swedbankPaymentRequest = (SwedbankPaymentRequest) paymentRequest;

        Map<String, String> authParams = paymentQueueMessage.getAuthParams();
        String accessToken = swedbankAuthenticationService.getAccessToken(authParams);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", swedbankPaymentRequest.getBic());

        MultiValueMap<String, String> headers = getRequiredHeaders(accessToken, swedbankPaymentRequest);

        PaymentResponse paymentResponse = getPaymentResponseFromAPI(queryParams, headers, swedbankPaymentRequest);
        PaymentSubmission paymentSubmission =
                paymentSubmissionRepository.save(swedbankPaymentSubmissionMapper.mapToPaymentSubmission(paymentResponse));
        transactionQueueMessageSender.sendTransactionQueueMessage(
                paymentQueueMessage.getBankCode(), authParams, paymentSubmission, queueEntryId
        );
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private PaymentResponse getPaymentResponseFromAPI(MultiValueMap<String, String> queryParams,
                                                      MultiValueMap<String, String> headers,
                                                      SwedbankPaymentRequest swedbankPaymentRequest)
            throws GenericException, ApiException {
        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getPaymentsEndpointUrl(),
                queryParams,
                headers,
                swedbankPaymentRequest,
                MediaType.APPLICATION_JSON
        );
        checkIfResponseValid(response);
        return response.bodyToMono(SwedbankPaymentResponse.class).block();
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
