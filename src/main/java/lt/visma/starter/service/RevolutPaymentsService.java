package lt.visma.starter.service;

import lt.visma.starter.model.revolut.Receiver;

public interface RevolutPaymentsService {
    String makePayment(String senderAccountIBAN,
                       Receiver receiver,
                       double amount,
                       String currency,
                       String reference);
}
