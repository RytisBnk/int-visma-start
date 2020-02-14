package lt.visma.starter.service.impl;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.model.revolut.Receiver;
import lt.visma.starter.service.HttpRequestService;
import lt.visma.starter.service.RevolutAccountsService;
import lt.visma.starter.service.RevolutPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RevolutPaymentServiceImpl implements RevolutPaymentsService {
    private RevolutConfigurationProperties configurationProperties;
    private RevolutAccountsService revolutAccountsService;
    private HttpRequestService httpRequestService;

    @Autowired
    public RevolutPaymentServiceImpl(RevolutConfigurationProperties configurationProperties, RevolutAccountsService revolutAccountsService, HttpRequestService httpRequestService) {
        this.configurationProperties = configurationProperties;
        this.revolutAccountsService = revolutAccountsService;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public String makePayment(String senderAccountIBAN, Receiver receiver, double amount, String currency, String reference) {
        throw new NotImplementedException();
    }
}
