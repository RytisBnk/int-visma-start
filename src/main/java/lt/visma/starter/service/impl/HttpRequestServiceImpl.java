package lt.visma.starter.service.impl;

import lt.visma.starter.service.HttpRequestService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {
    @Override
    public ClientResponse httpGetRequest(String baseUrl,
                                         String resource,
                                         MultiValueMap<String, String> queryParameters,
                                         MultiValueMap<String, String> headers) {
        WebClient client = WebClient.builder().baseUrl(baseUrl).build();

        WebClient.RequestHeadersSpec<?> request = client
                .get()
                .uri(uriBuilder -> uriBuilder.path(resource).queryParams(queryParameters).build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headers);
                });

        return request.exchange().block();
    }

    @Override
    public ClientResponse httpPostRequest(String baseUrl,
                                          String resource,
                                          MultiValueMap<String, String> queryParameters,
                                          MultiValueMap<String, String> headers,
                                          MultiValueMap<String, String> requestBody,
                                          MediaType contentType) {
        WebClient client = WebClient.builder().baseUrl(baseUrl).build();

        WebClient.RequestHeadersSpec<?> request = client
                .post()
                .uri(resource)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(contentType)
                .body(BodyInserters.fromFormData(requestBody));
        return request.exchange().block();
    }
}
