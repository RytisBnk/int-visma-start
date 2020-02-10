package lt.visma.starter.service.impl;

import lt.visma.starter.service.RevolutAccountsService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RevolutAccountServiceImpl implements RevolutAccountsService {
    @Override
    public String getAccounts(String accessToken) {
        URL requestUrl = null;
        try {
            requestUrl = new URL("https://sandbox-b2b.revolut.com/api/1.0/accounts");
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
