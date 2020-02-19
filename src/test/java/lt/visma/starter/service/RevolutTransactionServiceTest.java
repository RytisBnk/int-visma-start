package lt.visma.starter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.*;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.revolut.RevolutTransactionServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RevolutTransactionServiceTest extends MockWebServerTest {
    @Mock
    private RevolutConfigurationProperties configurationProperties;

    private RevolutTransactionServiceImpl revolutTransactionService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        HttpRequestService httpRequestService = new HttpRequestServiceImpl();
        revolutTransactionService = new RevolutTransactionServiceImpl(httpRequestService, configurationProperties);
    }

    @Test
    public void getTransactions_OKResponse_ReturnsTransactionList() throws JsonProcessingException, GenericException, ApiException {
        mockWebServer.enqueue(getMockResponse(getSampleTransactionList(), 200));

        whenMockApiUrl();

        List<Transaction> transactions = revolutTransactionService.getTransactions("", "", "");
        assertNotNull(transactions);
        assertTrue(transactions.size() != 0);
    }

    @Test
    public void getTransactions_BadRequestResponse_ThrowsApiException() throws JsonProcessingException {
        mockWebServer.enqueue(getMockResponse(getStandardErrorResponseObject(), 400));

        whenMockApiUrl();

        assertThrows(ApiException.class, () -> revolutTransactionService.getTransactions("", "", ""));
    }

    @Test
    public void getTransactionById_OKResponse_ReturnsSingleTransaction() throws JsonProcessingException, GenericException, ApiException {
        mockWebServer.enqueue(getMockResponse(getSampleTransactionList().get(0), 200));

        whenMockApiUrl();

        RevolutTransaction transaction = (RevolutTransaction) revolutTransactionService.getTransactionById("", "transactionId", "REVOGB21");
        assertNotNull(transaction);
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getReuestId());
        assertNotNull(transaction.getLegs());
    }

    @Test
    public void getTransactionById_BadRequestResponse_ThrowsApiException() throws JsonProcessingException {
        mockWebServer.enqueue(getMockResponse(getStandardErrorResponseObject(), 400));

        whenMockApiUrl();

        assertThrows(ApiException.class, () ->
                revolutTransactionService.getTransactionById("", "transactionId", "REVOGB21"));
    }

    private MockResponse getMockResponse(Object requestBody, int statusCode) throws JsonProcessingException {
        MockResponse response = new MockResponse();
        response.setResponseCode(statusCode);
        response.setBody((new ObjectMapper()).writeValueAsString(requestBody));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response;
    }

    private void whenMockApiUrl() {
        when(configurationProperties.getApiURL()).thenReturn("http://localhost:" + mockWebServer.getPort());
    }

    private List<RevolutTransaction> getSampleTransactionList() {
        List<RevolutTransaction> transactions = new ArrayList<>();
        TransactionLeg leg = new TransactionLeg(
                UUID.randomUUID().toString(),
                200,
                "EUR",
                "221.17",
                "USD",
                UUID.randomUUID().toString(),
                new Counterparty(UUID.randomUUID().toString(), UUID.randomUUID().toString(), CounterpartyType.REVOLUT),
                "Invoice payment #69",
                20546.56
        );
        List<TransactionLeg> legs = new ArrayList<>();
        legs.add(leg);

        RevolutTransaction transaction = new RevolutTransaction(
                UUID.randomUUID().toString(),
                RevolutTransactionType.CARD_PAYMENT,
                UUID.randomUUID().toString(),
                PaymentState.PENDING,
                null,
                "2020-01-01", "2020-01-01", null, null,
                null, null, legs, null
        );
        transactions.add(transaction);
        return transactions;
    }

    private ResponseError getStandardErrorResponseObject() {
        RevolutApiError error = new RevolutApiError();
        error.setCode(3000);
        error.setMessage("Error message");

        return error;
    }
}
