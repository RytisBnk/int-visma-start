package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.AuthenticationFailedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.RevolutAccessToken;
import lt.visma.starter.model.RevolutAccount;
import lt.visma.starter.model.RevolutApiError;
import lt.visma.starter.service.RevolutAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RevolutAccountServiceImpl implements RevolutAccountsService {
    @Autowired
    private RevolutConfigurationProperties configurationProperties;

    @Override
    public List<RevolutAccount> getAccounts(RevolutAccessToken accessToken) {
        WebClient client = WebClient.builder()
                .baseUrl(configurationProperties.getApiURL())
                .build();

        ClientResponse response = client
                .get()
                .uri("/accounts")
                .header("Authorization", "Bearer " + accessToken.getAccess_token())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .block();

        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.OK) {
            RevolutAccount[] accounts = response.bodyToMono(RevolutAccount[].class).block();
            return accounts != null ? Arrays.asList(accounts) : new ArrayList<>();
        }
        else {
            RevolutApiError apiError = response.bodyToMono(RevolutApiError.class).block();
            throw new AuthenticationFailedException(apiError != null ? apiError.getError_description() : "");
        }
    }
}
