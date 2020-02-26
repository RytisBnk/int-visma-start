package lt.visma.starter.service.revolut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.*;
import lt.visma.starter.model.revolut.entity.Counterparty;
import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import lt.visma.starter.model.revolut.entity.TransactionLeg;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.MockWebServerTest;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.revolut.RevolutAuthenticationService;
import lt.visma.starter.service.impl.revolut.RevolutTransactionService;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RevolutTransactionServiceTest extends MockWebServerTest {
    @Mock
    private RevolutConfigurationProperties configurationProperties;

    private RevolutTransactionService revolutTransactionService;

    @Mock
    private RevolutAuthenticationService authenticationService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        HttpRequestService httpRequestService = new HttpRequestServiceImpl();
        revolutTransactionService = new RevolutTransactionService(httpRequestService, configurationProperties, authenticationService);
    }

    @Test
    public void getTransactions_OKResponse_ReturnsTransactionList() throws JsonProcessingException, GenericException, ApiException {
        mockWebServer.enqueue(getMockResponse(getSampleTransactionList(), 200));

        whenMockApiUrl();

        List<Transaction> transactions = revolutTransactionService.getTransactions("", "", new HashMap<>());
        assertNotNull(transactions);
        assertTrue(transactions.size() != 0);
    }

    @Test
    public void getTransactions_BadRequestResponse_ThrowsApiException() throws JsonProcessingException {
        mockWebServer.enqueue(getMockResponse(getStandardErrorResponseObject(), 400));

        whenMockApiUrl();

        assertThrows(ApiException.class, () -> revolutTransactionService.getTransactions("", "", new HashMap<>()));
    }

    @Test
    public void getTransactionById_OKResponse_ReturnsSingleTransaction() throws JsonProcessingException, GenericException, ApiException {
        mockWebServer.enqueue(getMockResponse(getSampleTransactionList().get(0), 200));

        whenMockApiUrl();

        RevolutTransaction transaction = (RevolutTransaction)
                revolutTransactionService.getTransactionById("", "", new HashMap<>());
        assertNotNull(transaction);
        assertNotNull(transaction.getId());
        assertNotNull(transaction.getCreatedAt());
        assertNotNull(transaction.getReuestId());
        assertNotNull(transaction.getLegs());
    }

    @Test
    public void getTransactionById_BadRequestResponse_ThrowsApiException() throws JsonProcessingException {
        mockWebServer.enqueue(getMockResponse(getStandardErrorResponseObject(), 400));

        whenMockApiUrl();

        assertThrows(ApiException.class, () ->
                revolutTransactionService.getTransactionById("", "", new HashMap<>()));
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

        List<TransactionLeg> legs = new ArrayList<>();
        TransactionLeg leg = new TransactionLeg();
        leg.setLegId("1-1-1-1");
        leg.setAmount(200);
        leg.setCurrency("EUR");
        leg.setBillAmount("221.17");
        leg.setBillCurrency("USD");
        leg.setAccountId("1-1-1-1");
        Counterparty counterparty = new Counterparty();
        counterparty.setId("1-1-1-1");
        counterparty.setAccountId("2-2-2-2");
        counterparty.setType(CounterpartyType.REVOLUT);
        leg.setCounterparty(counterparty);
        leg.setDescription("Invoice payment #1");
        leg.setBalance(20546.56);
        legs.add(leg);

        RevolutTransaction transaction = new RevolutTransaction();
        transaction.setLegs(legs);
        transaction.setId(UUID.randomUUID().toString());
        transaction.setType(RevolutTransactionType.CARD_PAYMENT);
        transaction.setReuestId(UUID.randomUUID().toString());
        transaction.setState(PaymentState.PENDING);
        transaction.setCreatedAt("2020-01-01");
        transaction.setUpdatedAt("202-01-01");

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
