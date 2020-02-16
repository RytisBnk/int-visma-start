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
                                          Object requestBody,
                                          MediaType contentType) {
        WebClient client = WebClient.builder().baseUrl(baseUrl).build();

        WebClient.RequestHeadersSpec<?> request = buildPostRequest(client, resource, headers,queryParameters,requestBody,contentType);

        return request.exchange().block();
    }

    private WebClient.RequestHeadersSpec<?> buildPostRequest(WebClient client,
                                                             String resource,
                                                             MultiValueMap<String, String> headers,
                                                             MultiValueMap<String, String> queryParameters,
                                                             Object requestBody,
                                                             MediaType contentType) {
        if (requestBody == null) {
            return client.post()
                    .uri(uriBuilder -> uriBuilder.path(resource).queryParams(queryParameters).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .contentType(contentType);
        }
        return client.post()
                .uri(uriBuilder -> uriBuilder.path(resource).queryParams(queryParameters).build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .contentType(contentType)
                .body(BodyInserters.fromValue(requestBody));
    }
}
