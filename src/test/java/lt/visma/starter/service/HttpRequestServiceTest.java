package lt.visma.starter.service;

import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class HttpRequestServiceTest {
    private MockWebServer mockWebServer;

    private HttpRequestService httpRequestService;

    private final String URL = "http://localhost";
    private final String RESOURCE_PATH = "/test";

    private final String FIRST_QUERY_PARAM_NAME = "param1";
    private final String FIRST_QEURY_PARAM_VALUE = "firstValue";

    private final String SECOND_QUERY_PARAM_NAME = "param2";
    private final String SECOND_QUERY_PARAM_VALUE = "secondValue";

    private final String HEADER_NAME = "Authorization";
    private final String HEADER_VALUE = "Bearer tokenHere";
    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        httpRequestService = new HttpRequestServiceImpl();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void validGetRequestTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse());

        ClientResponse clientResponse = httpRequestService.httpGetRequest(
                String.format("%s:%s", URL ,mockWebServer.getPort()),
                RESOURCE_PATH,
                getTestQueryParams(),
                getTestHeaders()
        );

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        String expectedPath = String.format("%s?%s=%s&%s=%s",
                RESOURCE_PATH,
                FIRST_QUERY_PARAM_NAME, FIRST_QEURY_PARAM_VALUE,
                SECOND_QUERY_PARAM_NAME, SECOND_QUERY_PARAM_VALUE);
        assertEquals(expectedPath, recordedRequest.getPath());
        assertEquals(HEADER_VALUE, recordedRequest.getHeader(HEADER_NAME));
    }

    @Test
    public void validPostRequestTest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse());

        String json = "{\"TestKey\" : \"TestValue\"}";

        ClientResponse response = httpRequestService.httpPostRequest(
                String.format("%s:%s", URL, mockWebServer.getPort()),
                RESOURCE_PATH,
                getTestQueryParams(),
                getTestHeaders(),
                json,
                MediaType.APPLICATION_JSON
        );

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        String expectedPath = String.format("%s?%s=%s&%s=%s",
                RESOURCE_PATH,
                FIRST_QUERY_PARAM_NAME, FIRST_QEURY_PARAM_VALUE,
                SECOND_QUERY_PARAM_NAME, SECOND_QUERY_PARAM_VALUE);
        assertEquals(expectedPath, recordedRequest.getPath());
        assertEquals(HEADER_VALUE, recordedRequest.getHeader(HEADER_NAME));
        assertNotNull(recordedRequest.getBody());
    }

    private MultiValueMap<String, String> getTestHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HEADER_NAME, HEADER_VALUE);

        return headers;
    }

    private MultiValueMap<String, String> getTestQueryParams() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(FIRST_QUERY_PARAM_NAME, FIRST_QEURY_PARAM_VALUE);
        queryParams.add(SECOND_QUERY_PARAM_NAME, SECOND_QUERY_PARAM_VALUE);

        return queryParams;
    }
}
