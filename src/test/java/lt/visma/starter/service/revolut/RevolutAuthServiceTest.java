package lt.visma.starter.service.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.revolut.RovolutAuthServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RevolutAuthServiceTest {
    private HttpRequestService httpRequestService = new HttpRequestServiceImpl();
    @Mock
    private RevolutConfigurationProperties configurationProperties;
    private RovolutAuthServiceImpl revolutAuthenticationService;

    private final String TEST_EXPECTED_JWT_VALUE = "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tIiwic3ViIjoiQUFBQSIsImlzcyI6InRlc3QuY29tIn0.jrOKOEzK4F4YXR8AWfEpu2Rl0N9NNDeBfZ3WhsXzImo-c6Muau_9BAnHw7gsyAUd4pmB2_5TIk0dtqgQZlTLbDs5QxtHIrhYp2Ja9QpKlML6v0-I3UZG_Kka7uczdEeV3wNn3kMW_VIz4WwxFCHRdvCrlIZ8x8uFNlCFyEp9M70";
    private final String SANDBOX_JWT_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL3Jldm9sdXQuY29tIiwic3ViIjoiaXpycXkwNzZ0VE9mSnQwQWJMZEtVcWMyMzF4bE1VYmU1TU5iZE85bFJ3TSIsImlzcyI6InJldm9sdXQtand0LXNhbmRib3guZ2xpdGNoLm1lIn0.n7n-VkgTXR1NHWg-AVuZdnlztCYIvfyWo5_RtcqgBTkl7ur17Ys5wlyZ8n9d9SJKSWZ_5ONLUrY4uUW9jH_S7_MT-YQYoia5rfmnLOMW1TZGNzMlxRvZ1Usi_oFbFbsppOi2ewimNSUEdCRW5UIilpV9zkZn7yxyVa0Vf3HwQnM";
    private final String JWT_TEST_AUD = "https://example.com";
    private final String JWT_TEST_ISS = "test.com";
    private final String JWT_TEST_CLIENTID = "AAAA";
    private final String PRIVATE_KEY_FILEPATH = "./private_key.der";

    private final String JWT_REAL_AUD = "https://revolut.com";
    private final String JWT_REAL_ISS = "revolut-jwt-sandbox.glitch.me";
    private final String JWT_REAL_CLIENTID = "izrqy076tTOfJt0AbLdKUqc231xlMUbe5MNbdO9lRwM";

    private final String CLIENT_ID = "izrqy076tTOfJt0AbLdKUqc231xlMUbe5MNbdO9lRwM";
    private final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
    private final String REFRESH_TOKEN = "oa_sand_iPcIWTe-OgYbOj-p48RN1Y7uGUrlc3y3c-3LIg0eRYs";
    private final String API_URL = "https://sandbox-b2b.revolut.com/api/1.0";
    private final String AUTH_URL = "/auth/token";

    @Test
    public void testJwtValidity() throws GenericException {
        when(configurationProperties.getAud()).thenReturn(JWT_TEST_AUD);
        when(configurationProperties.getIss()).thenReturn(JWT_TEST_ISS);
        when(configurationProperties.getClientId()).thenReturn(JWT_TEST_CLIENTID);
        when(configurationProperties.getPrivateKeyFilepath()).thenReturn(PRIVATE_KEY_FILEPATH);

        assertEquals(TEST_EXPECTED_JWT_VALUE,  revolutAuthenticationService.getJWTToken());
    }

    @Test
    public void testAccessTokenRefreshing() throws GenericException, ApiException {
        when(configurationProperties.getClientId()).thenReturn(CLIENT_ID);
        when(configurationProperties.getClientAssertionType()).thenReturn(CLIENT_ASSERTION_TYPE);
        when(configurationProperties.getRefreshToken()).thenReturn(REFRESH_TOKEN);
        when(configurationProperties.getApiURL()).thenReturn(API_URL);
        when(configurationProperties.getAuthenticationEndpointUrl()).thenReturn(AUTH_URL);
        when(configurationProperties.getPrivateKeyFilepath()).thenReturn(PRIVATE_KEY_FILEPATH);

        when(configurationProperties.getAud()).thenReturn(JWT_REAL_AUD);
        when(configurationProperties.getIss()).thenReturn(JWT_REAL_ISS);
        when(configurationProperties.getClientId()).thenReturn(JWT_REAL_CLIENTID);
        when(configurationProperties.getPrivateKeyFilepath()).thenReturn(PRIVATE_KEY_FILEPATH);

        String accessToken = revolutAuthenticationService.getAccessToken(new HashMap<>());
        assertNotNull(accessToken);
    }

    @Before
    public void init() {
        initMocks(this);
        revolutAuthenticationService = new RovolutAuthServiceImpl(configurationProperties, httpRequestService);
    }
}
