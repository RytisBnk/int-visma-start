package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.OperationNotSupportedException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.swedbank.SwedbankPaymentTransaction;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.util.HTTPUtils;
import lt.visma.starter.util.TimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SwedbankTransactionService implements TransactionService {
    private final HttpRequestService httpRequestService;
    private final SwedbankConfigurationProperties configurationProperties;
    private final AuthenticationService swedbankAuthenticationService;

    public SwedbankTransactionService(HttpRequestService httpRequestService,
                                      SwedbankConfigurationProperties configurationProperties,
                                      AuthenticationService swedbankAuthenticationService) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.swedbankAuthenticationService = swedbankAuthenticationService;
    }

    @Override
    public List<Transaction> getTransactions(String from, String to, Map<String, String> authParams) throws GenericException, ApiException, OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public Transaction getTransactionById(String transactionId, String bankCode, Map<String, String> authParams) throws GenericException, ApiException {
        String accessToken = swedbankAuthenticationService.getAccessToken(authParams);
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
        SwedbankPaymentTransaction transaction = response.bodyToMono(SwedbankPaymentTransaction.class).block();
        if (transaction != null) {
            transaction.setId(transactionId);
        }
        return transaction;
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
