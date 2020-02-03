package lt.visma.starter.service.impl;

import lt.visma.starter.service.BasicService;
import org.springframework.stereotype.Service;

@Service
public class BasicServiceImpl implements BasicService {
    public String getData() {
        return "Sample text";
    }

    @Override
    public String writeData(String data) {
        return "Data you sent: " + data;
    }
}
