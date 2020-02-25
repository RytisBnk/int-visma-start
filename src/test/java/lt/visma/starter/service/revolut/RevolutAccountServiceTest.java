package lt.visma.starter.service.revolut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.BankingAccount;
import lt.visma.starter.model.revolut.RevolutAccount;
import lt.visma.starter.model.revolut.RevolutResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.MockWebServerTest;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.revolut.RevolutAccountService;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RevolutAccountServiceTest extends MockWebServerTest {
    @Mock
    private RevolutConfigurationProperties configurationProperties;
    private RevolutAccountService revolutAccountService;

    @Mock
    private AuthenticationService revolutAuthenticationService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        initMocks(this);
        HttpRequestService httpRequestService = new HttpRequestServiceImpl();
        revolutAccountService = new RevolutAccountService(configurationProperties, httpRequestService, revolutAuthenticationService);
    }

    @Test
    public void getBankingAccounts_OKResponse_ReturnBankingAccounts()
            throws JsonProcessingException, GenericException, ApiException {
        MockResponse response = new MockResponse();
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setBody((new ObjectMapper()).writeValueAsString(getValidResponseBody()));
        mockWebServer.enqueue(response);

        whenMockApiUrl();

        List<BankingAccount> accounts = revolutAccountService.getBankingAccounts( null);
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
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockWebServer.enqueue(response);

        whenMockApiUrl();

        assertThrows(ApiException.class, () -> revolutAccountService.getBankingAccounts( null));
    }

    private List<BankingAccount> getValidResponseBody() {
        List<BankingAccount> bankingAccounts = new ArrayList<>();

        RevolutAccount account = new RevolutAccount();
        account.setName("Spending account");
        account.setBalance(20000);
        account.setCurrency("EUR");
        account.setState("active");
        account.setPublic(true);
        account.setCreatedAt("2020-01-01");
        account.setUpdatedAt("2020-02-01");
        bankingAccounts.add(account);

        return bankingAccounts;
    }

    private void whenMockApiUrl() {
        when(configurationProperties.getApiURL()).thenReturn("http://localhost:" + mockWebServer.getPort());
    }
}
