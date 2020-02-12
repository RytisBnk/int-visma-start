package lt.visma.starter.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.AuthenticationFailedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.model.RevolutAccessToken;
import lt.visma.starter.model.RevolutApiError;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.RovolutAuthenticationService;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class RovolutAuthServiceImpl implements RovolutAuthenticationService {
    @Autowired
    private RevolutConfigurationProperties configurationProperties;

    @Autowired
    private HttpRequestService httpRequestService;

    @Override
    public String getJWTToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "revolut-jwt-sandbox.glitch.me");
        claims.put("aud", "https://revolut.com");
        claims.put("sub", configurationProperties.getClientId());


        byte[] keyBytes = new byte[0];
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

    @Override
    public RevolutAccessToken getAccessToken() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", configurationProperties.getAuthorisationCode());
        params.add("client_id", configurationProperties.getClientId());
        params.add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        params.add("client_assertion", getJWTToken());

        return sendAuthenticationRequest(params);
    }

    @Override
    public RevolutAccessToken refreshAccessToken() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", configurationProperties.getRefreshToken());
        params.add("client_id", configurationProperties.getClientId());
        params.add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        params.add("client_assertion", getJWTToken());

        return sendAuthenticationRequest(params);
    }

    private RevolutAccessToken sendAuthenticationRequest(Object requestBody) {
        ClientResponse response = httpRequestService.httpPostRequest(
                configurationProperties.getApiURL(),
                "/auth/token",
                null,
                null,
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
            RevolutApiError apiError = response.bodyToMono(RevolutApiError.class).block();
            throw new AuthenticationFailedException(apiError != null ? apiError.getError_description() : "");
        }
    }
}
