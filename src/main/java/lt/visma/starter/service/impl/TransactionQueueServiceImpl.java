package lt.visma.starter.service.impl;


import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.mapper.PaymentMapper;
import lt.visma.starter.mapper.factory.PaymentMapperFactory;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.dto.TransactionQueueMessage;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.model.entity.PaymentQueueEntry;
import lt.visma.starter.model.entity.QueueEntryState;
import lt.visma.starter.repository.PaymentQueueEntryRepository;
import lt.visma.starter.repository.PaymentRepository;
import lt.visma.starter.service.TransactionQueueService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.service.factory.TransactionServiceFactory;
import org.springframework.stereotype.Service;

@Service
public class TransactionQueueServiceImpl implements TransactionQueueService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapperFactory paymentMapperFactory;
    private final TransactionServiceFactory transactionServiceFactory;
    private final PaymentQueueEntryRepository paymentQueueEntryRepository;

    public TransactionQueueServiceImpl(PaymentRepository paymentRepository,
                                       PaymentMapperFactory paymentMapperFactory,
                                       TransactionServiceFactory transactionServiceFactory,
                                       PaymentQueueEntryRepository paymentQueueEntryRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapperFactory = paymentMapperFactory;
        this.transactionServiceFactory = transactionServiceFactory;
        this.paymentQueueEntryRepository = paymentQueueEntryRepository;
    }


    @Override
    public void processTransactionQueueMessage(TransactionQueueMessage transactionQueueMessage)
            throws BankNotSupportedException, GenericException, ApiException, InvalidTransactionException {
        String bankCode = transactionQueueMessage.getBankCode();

        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        Transaction transaction = transactionService.getTransactionById(
                transactionQueueMessage.getPaymentSubmission().getPaymentId(),
                bankCode,
                transactionQueueMessage.getAuthParams()
        );

        PaymentMapper paymentMapper = paymentMapperFactory.getPaymentMapperService(transaction);
        Payment payment = paymentRepository.save(paymentMapper.mapToPersistentPaymentObject(transaction));

        PaymentQueueEntry paymentQueueEntry = paymentQueueEntryRepository
                .findById(transactionQueueMessage.getPaymentQueueId())
                .orElseThrow(GenericException::new);
        paymentQueueEntry.setStatus(QueueEntryState.COMPLETED);
        paymentQueueEntry.setPaymentId(payment.getId());
        paymentQueueEntryRepository.save(paymentQueueEntry);
    }
}
