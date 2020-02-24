package lt.visma.starter.service.swedbank;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.service.impl.swedbank.SwedbankAuthServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SwedbankAuthServiceTest {
    @Autowired
    private SwedbankAuthServiceImpl authenticationService;

    @Test
    public void getAccessTokenTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("psu-id", "ID");
        params.put("sca-method", "SMART_ID");

        String accessToken = authenticationService.getAccessToken(params);
        Assert.assertNotNull(accessToken);
    }

    @Test
    public void getAccessTokenFailsWithNoParams() {
        assertThrows(ApiException.class, () -> authenticationService.getAccessToken(new HashMap<>()));
    }

    @Test
    public void getAccessTokenFailsWithInvalidParams() {
        Map<String, String> params = new HashMap<>();
        params.put("sca-method", "invalid sca method");
        params.put("psu-id", "ID");

        assertThrows(ApiException.class, () -> authenticationService.getAccessToken(params));
    }
}
