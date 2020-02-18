package lt.visma.starter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.revolut.RevolutAccount;
import lt.visma.starter.model.revolut.RevolutResponseError;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.revolut.RevolutAccountServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RevolutAccountServiceTest {
    @Mock
    private RevolutConfigurationProperties configurationProperties;
    private RevolutAccountServiceImpl revolutAccountService;
    private HttpRequestService httpRequestService;

    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        initMocks(this);
        httpRequestService = new HttpRequestServiceImpl();
        revolutAccountService = new RevolutAccountServiceImpl(configurationProperties, httpRequestService);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getBankingAccounts_OKResponse_ReturnBankingAccounts()
            throws JsonProcessingException, GenericException, ApiException {
        MockResponse response = new MockResponse();
        response.setHeader("Content-Type", "application/json");
        response.setBody((new ObjectMapper()).writeValueAsString(getValidResponseBody()));
        mockWebServer.enqueue(response);

        when(configurationProperties.getApiURL()).thenReturn("http://localhost:" + mockWebServer.getPort());

        List<BankingAccount> accounts = revolutAccountService.getBankingAccounts("", null);
        assertNotNull(accounts);
        assertTrue(accounts.size() != 0);
    }

    @Test
    public void getBankingAccounts_BadRequestResponse_ThrowApiException() throws JsonProcessingException {
        RevolutResponseError revolutResponseError = new RevolutResponseError();
        revolutResponseError.setError("FORMAT_ERROR");
        revolutResponseError.setErrorDescription("Something went wrong");

        MockResponse response = new MockResponse();
        response.setResponseCode(400);
        response.setBody((new ObjectMapper()).writeValueAsString(revolutResponseError));
        response.setHeader("Content-type", "application/json");
        mockWebServer.enqueue(response);

        when(configurationProperties.getApiURL()).thenReturn("http://localhost:" + mockWebServer.getPort());

        assertThrows(ApiException.class, () -> revolutAccountService.getBankingAccounts("", null));
    }

    private List<BankingAccount> getValidResponseBody() {
        List<BankingAccount> bankingAccounts = new ArrayList<>();
        bankingAccounts.add(new RevolutAccount(
                UUID.randomUUID().toString(),
                "Spending account",
                20000,
                "EUR",
                "active",
                true,
                "2020-01-01", "2020-02-01"
        ));

        return bankingAccounts;
    }
}
