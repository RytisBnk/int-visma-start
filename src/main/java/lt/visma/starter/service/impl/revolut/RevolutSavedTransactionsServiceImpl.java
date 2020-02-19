package lt.visma.starter.service.impl.revolut;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.TransactionNotFoundException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.RevolutTransaction;
import lt.visma.starter.repository.RevolutPaymentRepository;
import lt.visma.starter.service.SavedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RevolutSavedTransactionsServiceImpl implements SavedTransactionsService {
    private RevolutPaymentRepository revolutPaymentRepository;
    private RevolutConfigurationProperties configurationProperties;

    @Autowired
    public RevolutSavedTransactionsServiceImpl(RevolutPaymentRepository revolutPaymentRepository, RevolutConfigurationProperties configurationProperties) {
        this.revolutPaymentRepository = revolutPaymentRepository;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(revolutPaymentRepository.findAll());
    }

    @Override
    public Optional<Transaction> getTransactionById(long id) throws TransactionNotFoundException {
        Optional<RevolutTransaction> revolutTransaction = revolutPaymentRepository.findById(id);
        if (!revolutTransaction.isPresent()) {
            throw new TransactionNotFoundException();
        }
        return Optional.of(revolutTransaction.get());
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws GenericException {
        if (! (transaction instanceof RevolutTransaction)) {
            throw new GenericException();
        }
        RevolutTransaction revolutTransaction = (RevolutTransaction) transaction;
        return revolutPaymentRepository.save(revolutTransaction);
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }
}
