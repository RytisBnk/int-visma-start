package lt.visma.starter.service;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface SavedPaymentService {
    Payment savePayment(Transaction payment) throws InvalidTransactionException;
    Optional<Payment> getPaymentById(String id);
    List<Payment> getPayments();
}
