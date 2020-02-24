package lt.visma.starter.service.swedbank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.ResponseError;
import lt.visma.starter.model.swedbank.AccountsListResponse;
import lt.visma.starter.model.swedbank.SwedbankAccount;
import lt.visma.starter.model.swedbank.SwedbankResponseError;
import lt.visma.starter.model.swedbank.TppMessage;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.MockWebServerTest;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.swedbank.SwedbankAccountsServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SwedbankAccountsServiceTest extends MockWebServerTest {
    @Mock
    private SwedbankConfigurationProperties configurationProperties;
    private SwedbankAccountsServiceImpl swedbankAccountsService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        HttpRequestService httpRequestService = new HttpRequestServiceImpl();
        swedbankAccountsService = new SwedbankAccountsServiceImpl(configurationProperties, httpRequestService);
    }

    @Test
    public void getBankingAccounts_OKResponseReceived_ReturnsListOfAccounts()
            throws JsonProcessingException, GenericException, ApiException {
        MockResponse response = new MockResponse();
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setBody((new ObjectMapper()).writeValueAsString(getSampleAccountsList()));
        mockWebServer.enqueue(response);

        whenMockApiUrl();

        List<BankingAccount> accounts = swedbankAccountsService.getBankingAccounts("", new HashMap<>());
        assertNotNull(accounts);
        assertTrue(accounts.size() != 0);
    }

    @Test
    public void getBankingAccounts_BadRequestResponseReceived_ThrowsApiException() throws JsonProcessingException {
        MockResponse response = new MockResponse();
        response.setResponseCode(400);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setBody((new ObjectMapper()).writeValueAsString(getSampleSwedbankAPIError()));
        mockWebServer.enqueue(response);

        whenMockApiUrl();

        assertThrows(ApiException.class, () -> swedbankAccountsService.getBankingAccounts("", new HashMap<>()));
    }

    private AccountsListResponse getSampleAccountsList() {
        List<SwedbankAccount> accounts = new ArrayList<>();
        SwedbankAccount account1 = new SwedbankAccount(
                "CACC",
                "EUR",
                "LT1111222233331111",
                "Name name",
                null,
                "1111-1111-1111-1111"
        );

        accounts.add(account1);
        AccountsListResponse accountsListResponse = new AccountsListResponse();
        accountsListResponse.setAccounts(accounts);
        return accountsListResponse;
    }

    private void whenMockApiUrl() {
        when(configurationProperties.getApiUrl()).thenReturn("http://localhost:" + mockWebServer.getPort());
    }

    private ResponseError getSampleSwedbankAPIError() {
        SwedbankResponseError responseError = new SwedbankResponseError();

        List<TppMessage> tppMessages = new ArrayList<>();
        TppMessage message = new TppMessage();
        message.setCategory("ERROR");
        message.setCode("FORMAT_ERROR");
        message.setText("Mandatory header is missing: Date");
        tppMessages.add(message);

        responseError.setTppMessages(tppMessages);
        return responseError;
    }
}
