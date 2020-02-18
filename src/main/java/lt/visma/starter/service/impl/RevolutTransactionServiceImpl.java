package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.RevolutApiError;
import lt.visma.starter.model.revolut.RevolutTransaction;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.util.HTTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class RevolutTransactionServiceImpl implements TransactionService {
    private HttpRequestService httpRequestService;
    private RevolutConfigurationProperties configurationProperties;

    private String[] supportedBanks = new String[] {"REVOGB21"};

    @Autowired
    public RevolutTransactionServiceImpl(HttpRequestService httpRequestService, RevolutConfigurationProperties configurationProperties) {
        this.httpRequestService = httpRequestService;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public List<Transaction> getTransactions(String accessToken, String from, String to) throws GenericException, ApiException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("from", from);
        queryParams.add("to", to);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiURL(),
                "/transactions",
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
    public Transaction getTransactionById(String accessToken, String transactionId) throws GenericException, ApiException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        HTTPUtils.addAuthorizationHeader(headers, accessToken);

        ClientResponse response = httpRequestService.httpGetRequest(
                configurationProperties.getApiURL(),
                "/transaction/" + transactionId,
                null,
                headers
        );

        checkIfResponseValid(response);
        return response.bodyToMono(RevolutTransaction.class).block();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(supportedBanks).contains(bankCode);
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
