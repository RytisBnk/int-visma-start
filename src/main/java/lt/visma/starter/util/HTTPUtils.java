package lt.visma.starter.util;

import org.springframework.util.MultiValueMap;

public class HTTPUtils {
    public static void addAuthorizationHeader(MultiValueMap<String, String> headers, String accessToken) {
        headers.add("Authorization", "Bearer " + accessToken);
    }
}
