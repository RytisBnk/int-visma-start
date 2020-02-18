package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.OperationNotSupportedException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.swedbank.SwedbankPaymentRequest;
import lt.visma.starter.model.swedbank.SwedbankPaymentTransaction;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.util.HTTPUtils;
import lt.visma.starter.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SwedbankTransactionService implements TransactionService {
    private HttpRequestService httpRequestService;
    private SwedbankConfigurationProperties configurationProperties;

    @Autowired
    public SwedbankTransactionService(HttpRequestService httpRequestService, SwedbankConfigurationProperties configurationProperties) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public List<Transaction> getTransactions(String accessToken, String from, String to) throws GenericException, ApiException, OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public Transaction getTransactionById(String accessToken, String transactionId, String bankCode) throws GenericException, ApiException {
        MultiValueMap<String, String> headers = getRequiredHeaders(accessToken);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("bic", bankCode);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiUrl(),
                configurationProperties.getPaymentDetailsEndpointUrl() + transactionId,
                queryParams,
                headers
        );
        checkIfResponseValid(response);
        return response.bodyToMono(SwedbankPaymentTransaction.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private MultiValueMap<String, String> getRequiredHeaders(String accessToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);
        headers.add("Date", TimeUtils.getCurrentServerTimeAsString());
        headers.add("X-request-ID", UUID.randomUUID().toString());

        return headers;
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() != HttpStatus.OK) {
            throw new ApiException(response.bodyToMono(SwedbankResponseError.class).block());
        }
    }
}
