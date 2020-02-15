package lt.visma.starter.service;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.revolut.RevolutAccessToken;
import lt.visma.starter.service.impl.HttpRequestServiceImpl;
import lt.visma.starter.service.impl.RovolutAuthServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RevolutAuthServiceTest {
    private HttpRequestService httpRequestService = new HttpRequestServiceImpl();
    @Mock
    private RevolutConfigurationProperties configurationProperties;

    private RovolutAuthenticationService revolutAuthenticationService;

    @Test
    public void testJwtValidity() throws GenericException {
        when(configurationProperties.getAud()).thenReturn("https://example.com");
        when(configurationProperties.getIss()).thenReturn("test.com");
        when(configurationProperties.getClientId()).thenReturn("AAAA");
        when(configurationProperties.getPrivateKeyFilepath()).thenReturn("./private_key.der");

        assertEquals("eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2V4YW1wbGUuY29tIiwic3ViIjoiQUFBQSIsImlzcyI6InRlc3QuY29tIn0.jrOKOEzK4F4YXR8AWfEpu2Rl0N9NNDeBfZ3WhsXzImo-c6Muau_9BAnHw7gsyAUd4pmB2_5TIk0dtqgQZlTLbDs5QxtHIrhYp2Ja9QpKlML6v0-I3UZG_Kka7uczdEeV3wNn3kMW_VIz4WwxFCHRdvCrlIZ8x8uFNlCFyEp9M70",
                revolutAuthenticationService.getJWTToken());
    }

    @Test
    public void testAccessTokenRefreshing() throws GenericException, ApiException {
        when(configurationProperties.getClientId()).thenReturn("izrqy076tTOfJt0AbLdKUqc231xlMUbe5MNbdO9lRwM");
        when(configurationProperties.getClientAssertionType()).thenReturn("urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        when(configurationProperties.getRefreshToken()).thenReturn("oa_sand_iPcIWTe-OgYbOj-p48RN1Y7uGUrlc3y3c-3LIg0eRYs");
        when(configurationProperties.getApiURL()).thenReturn("https://sandbox-b2b.revolut.com/api/1.0");

        RevolutAccessToken accessToken = revolutAuthenticationService.refreshAccessToken("eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL3Jldm9sdXQuY29tIiwic3ViIjoiaXpycXkwNzZ0VE9mSnQwQWJMZEtVcWMyMzF4bE1VYmU1TU5iZE85bFJ3TSIsImlzcyI6InJldm9sdXQtand0LXNhbmRib3guZ2xpdGNoLm1lIn0.n7n-VkgTXR1NHWg-AVuZdnlztCYIvfyWo5_RtcqgBTkl7ur17Ys5wlyZ8n9d9SJKSWZ_5ONLUrY4uUW9jH_S7_MT-YQYoia5rfmnLOMW1TZGNzMlxRvZ1Usi_oFbFbsppOi2ewimNSUEdCRW5UIilpV9zkZn7yxyVa0Vf3HwQnM");
        assertNotNull(accessToken);
        assertNotNull(accessToken.getAccessToken());
        assertNotNull(accessToken.getExpiresIn());
        assertNull(accessToken.getRefreshToken());
    }

    @Before
    public void init() {
        initMocks(this);
        revolutAuthenticationService = new RovolutAuthServiceImpl(configurationProperties, httpRequestService);
    }
}
