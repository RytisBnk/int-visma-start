package lt.visma.starter.service.swedbank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.OperationNotSupportedException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.swedbank.entity.SwedbankPaymentTransaction;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.MockWebServerTest;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.swedbank.SwedbankTransactionServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SwedbankTransactionServiceImplTest extends MockWebServerTest {
    @Mock
    private SwedbankConfigurationProperties configurationProperties;

    private SwedbankTransactionServiceImpl swedbankTransactionService;

    private final String TARGET_TRANSACTION_ID = "1-1-1-1";

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        HttpRequestService httpRequestService = new HttpRequestServiceImpl();
        swedbankTransactionService = new SwedbankTransactionServiceImpl(httpRequestService, configurationProperties);
    }

    @Test
    public void getTransactions_ThrowsOperationNotSupportedException() {
        assertThrows(OperationNotSupportedException.class,
                () -> swedbankTransactionService.getTransactions("", "", ""));
    }

    @Test
    public void getTransactionById_OKResponse_ReturnsTransaction() throws JsonProcessingException, GenericException, ApiException {
        MockResponse response = new MockResponse();
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setBody((new ObjectMapper()).writeValueAsString(getSampleTransaction()));
        mockWebServer.enqueue(response);

        whenMockApiUrl();

        Transaction transaction = swedbankTransactionService.getTransactionById("", TARGET_TRANSACTION_ID, "");
        assertNotNull(transaction);
        assertEquals(TARGET_TRANSACTION_ID, ((SwedbankPaymentTransaction) transaction).getId());
    }

    private void whenMockApiUrl() {
        when(configurationProperties.getApiUrl()).thenReturn("http://localhost:" + mockWebServer.getPort());
    }

    private SwedbankPaymentTransaction getSampleTransaction() {
        SwedbankPaymentTransaction transaction = new SwedbankPaymentTransaction();
        transaction.setId(TARGET_TRANSACTION_ID);


        return transaction;
    }
}
