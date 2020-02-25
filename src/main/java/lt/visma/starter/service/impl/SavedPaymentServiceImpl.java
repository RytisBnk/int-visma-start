package lt.visma.starter.service.impl;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.repository.PaymentRepository;
import lt.visma.starter.mapper.PaymentMapper;
import lt.visma.starter.service.SavedPaymentService;
import lt.visma.starter.mapper.factory.PaymentMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavedPaymentServiceImpl implements SavedPaymentService {
    private PaymentMapperFactory paymentMapperFactory;
    private PaymentRepository paymentRepository;

    @Autowired
    public SavedPaymentServiceImpl(PaymentMapperFactory paymentMapperFactory, PaymentRepository paymentRepository) {
        this.paymentMapperFactory = paymentMapperFactory;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment savePayment(Transaction transaction) throws InvalidTransactionException {
        PaymentMapper paymentMapper = paymentMapperFactory.getPaymentMapperService(transaction);
        return paymentRepository.save(paymentMapper.mapToPersistentPaymentObject(transaction));
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
