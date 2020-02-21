package lt.visma.starter.service.impl;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.repository.PaymentRepository;
import lt.visma.starter.service.PaymentMapperService;
import lt.visma.starter.service.SavedPaymentService;
import lt.visma.starter.service.factory.PaymentMapperServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedPaymentServiceImpl implements SavedPaymentService {
    private PaymentMapperServiceFactory paymentMapperServiceFactory;
    private PaymentRepository paymentRepository;

    @Autowired
    public SavedPaymentServiceImpl(PaymentMapperServiceFactory paymentMapperServiceFactory, PaymentRepository paymentRepository) {
        this.paymentMapperServiceFactory = paymentMapperServiceFactory;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment savePayment(Transaction transaction) throws InvalidTransactionException {
        PaymentMapperService paymentMapperService = paymentMapperServiceFactory.getPaymentMapperService(transaction);
        return paymentRepository.save(paymentMapperService.mapToPersistentPaymentObject(transaction));
    }

    @Override
    public Optional<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }
}
