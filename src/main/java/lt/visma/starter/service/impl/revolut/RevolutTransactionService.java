package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.RevolutApiError;
import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.util.HTTPUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RevolutTransactionService implements TransactionService {
    private final HttpRequestService httpRequestService;
    private final RevolutConfigurationProperties configurationProperties;
    private final AuthenticationService revolutAuthenticationService;

    public RevolutTransactionService(HttpRequestService httpRequestService,
                                     RevolutConfigurationProperties configurationProperties,
                                     RevolutAuthenticationService revolutAuthenticationService) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
        this.revolutAuthenticationService = revolutAuthenticationService;
    }

    @Override
    public List<Transaction> getTransactions(String from, String to, Map<String, String> authParams) throws GenericException, ApiException {
        String accessToken = revolutAuthenticationService.getAccessToken(authParams);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("from", from);
        queryParams.add("to", to);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiURL(),
                configurationProperties.getMultipleTransactionsEndpoint(),
                queryParams,
                headers
        );

        checkIfResponseValid(response);
        RevolutTransaction[] transactions = response.bodyToMono(RevolutTransaction[].class).block();
        if (transactions == null) {
            throw new GenericException();
        }
        return Arrays.asList(transactions);
    }

    @Override
    public Transaction getTransactionById(String transactionId, String bankCode, String accessToken) throws GenericException, ApiException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiURL(),
                configurationProperties.getIndividualTransactionEndpoint() + transactionId,
                null,
                headers
        );

        checkIfResponseValid(response);
        return response.bodyToMono(RevolutTransaction.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }

    private void checkIfResponseValid(ClientResponse response) throws GenericException, ApiException {
        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() != HttpStatus.OK) {
            ResponseError responseError = response.bodyToMono(RevolutApiError.class).block();
            throw new ApiException(responseError);
        }
    }
}
