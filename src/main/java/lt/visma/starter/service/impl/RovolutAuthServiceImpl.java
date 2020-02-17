package lt.visma.starter.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.RevolutApiException;
import lt.visma.starter.model.revolut.RevolutAccessToken;
import lt.visma.starter.model.revolut.RevolutResponseError;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class RovolutAuthServiceImpl implements AuthenticationService {
    private RevolutConfigurationProperties configurationProperties;
    private HttpRequestService httpRequestService;

    private String[] supportedBanks = new String[] {"REVOGB21"};

    @Autowired
    public RovolutAuthServiceImpl(RevolutConfigurationProperties configurationProperties, HttpRequestService httpRequestService) {
        this.configurationProperties = configurationProperties;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public String getAccessToken(Map<String, String> authenticationParams) throws GenericException, ApiException {
        MultiValueMap<String, Object> params = getStandardHeaders(getJWTToken());
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", configurationProperties.getRefreshToken());

        return sendAuthenticationRequest(params).getAccessToken();
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(supportedBanks).contains(bankCode);
    }

    public String getJWTToken() throws GenericException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", configurationProperties.getIss());
        claims.put("aud", configurationProperties.getAud());
        claims.put("sub", configurationProperties.getClientId());

        byte[] keyBytes;
        try {
            keyBytes = Files.readAllBytes(Paths.get(configurationProperties.getPrivateKeyFilepath()));
        } catch (IOException e) {
            throw new GenericException();
        }
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            KeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.RS256, privateKey)
                    .compact();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new GenericException();
        }
    }

    private MultiValueMap<String, Object> getStandardHeaders(String jwtToken) {
        MultiValueMap<String, Object> standardHeaders = new LinkedMultiValueMap<>();
        standardHeaders.add("client_id", configurationProperties.getClientId());
        standardHeaders.add("client_assertion_type", configurationProperties.getClientAssertionType());
        standardHeaders.add("client_assertion", jwtToken);

        return standardHeaders;
    }

    private RevolutAccessToken sendAuthenticationRequest(Object requestBody) throws GenericException, ApiException {
        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiURL(),
                configurationProperties.getAuthenticationEndpointUrl(),
                null,
                new LinkedMultiValueMap<>(),
                requestBody,
                MediaType.APPLICATION_FORM_URLENCODED
        );

        if (response == null) {
            throw new GenericException();
        }
        if (response.statusCode() == HttpStatus.OK) {
            return response.bodyToMono(RevolutAccessToken.class).block();
        }
        else {
            RevolutResponseError apiError = response.bodyToMono(RevolutResponseError.class).block();
            throw new RevolutApiException(apiError);
        }
    }
}
