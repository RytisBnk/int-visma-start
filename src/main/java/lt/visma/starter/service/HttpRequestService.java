package lt.visma.starter.service;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

public interface HttpRequestService {
    ClientResponse httpGetRequest(String baseUrl,
                                  String resource,
                                  MultiValueMap<String, String> queryParameters,
                                  MultiValueMap<String, String> headers);
    ClientResponse httpPostRequest(String baseUrl,
                                   String resource,
                                   MultiValueMap<String, String> queryParameters,
                                   MultiValueMap<String, String> headers,
                                   MultiValueMap<String, String> requestBody,
                                   MediaType contentType);
}
