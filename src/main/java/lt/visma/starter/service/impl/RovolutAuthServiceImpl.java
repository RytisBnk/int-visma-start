package lt.visma.starter.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lt.visma.starter.service.RovolutAuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class RovolutAuthServiceImpl implements RovolutAuthenticationService {
    @Value("${revolut.clientId}")
    private String CLIENT_ID;

    @Override
    public String getJWTToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "revolut-jwt-sandbox.glitch.me");
        claims.put("aud", "https://revolut.com");
        claims.put("sub", CLIENT_ID);


        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Files.readAllBytes(Paths.get("./private_key.der"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            KeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
            return Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.RS256, privateKey)
                    .compact();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
