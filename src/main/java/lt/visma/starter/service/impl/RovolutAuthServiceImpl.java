package lt.visma.starter.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lt.visma.starter.service.RovolutAuthenticationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RovolutAuthServiceImpl implements RovolutAuthenticationService {
    private final String key = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXAIBAAKBgQCj8P4z/Q8BeBcBsNa8mwRn3eslliegr1HNPYTbnd90354Pv/tL\n" +
            "XphAQ/zbD0rk8YUFjv8UMqrPtCi0vaxXokQV6fXwRLSE+UIaPVhw/n3o+xnjbMhI\n" +
            "RysZgfYJZ0hVvJn5k2IfjFDl/aTWXO49CeFHQLTPfnLLi319N6NgyLSFlwIDAQAB\n" +
            "AoGAXCh4Le9+xPumKzwmqqWV53GX42wyYKDNokuH6REpcYDXUNFCSY8mlVHizZ4e\n" +
            "Iu8mMQa+m7MT1GjZTSkmvaipDqYs9f7lD5Nio6DzyyT7xvWIOVvw4fPONxBzQVtO\n" +
            "KzdwQZkG4qZWMqOVvlRtvkIpFYP1RT98zOnrcIhg7Fj0wLECQQDSHPac814mYMHT\n" +
            "w+paXvjRo0RyWsMgUDyDl59MnczHwBXXmVGbOsdD9iIUzmeER8BGP6S0UeeJyDwI\n" +
            "k/NZNH1pAkEAx76m7JMnh4h7+N/+97xuwLA6Jnl1eHXSa39GzrLM54Nc9cE0afKV\n" +
            "iX1wHrWIp7dBotzj4QKud+Bjtq0qrCmK/wJBAJC3eDhj+z3tuLK1pu11qmUh0dv9\n" +
            "nZXdDcEJJqQrr8cJC0JDDI0hL+wkVBcGjw/yJ4F7BmNYYmRo8MhrttmDKLkCQHRU\n" +
            "PLMbILJ2cn0HN17gh0ABYlx18EwvklS402weBefvQXx/AR+TADQKsfmwj93dhIRt\n" +
            "UOoCvcljDa+eOnP53dECQBHLKusE8hWBPhk5ye1Mn8Jk1abSy4/BHHaC0gfG2zEJ\n" +
            "WBQA3sYleKnACiJgtMELbdwfFUz2WXmVNDX+MlKSI80=\n" +
            "-----END RSA PRIVATE KEY-----\n";

    @Override
    public String getJWTToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "revolut-jwt-sandbox.glitch.me");
        claims.put("aud", "https://revolut.com");
        claims.put("sub", "kclqA3nZgMx9A18wKJBNJ44r1wY9hqjuwmIrMzcnQCU");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
