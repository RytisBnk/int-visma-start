package lt.visma.starter.service.impl.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.TransactionNotFoundException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.swedbank.SwedbankPaymentTransaction;
import lt.visma.starter.repository.SwedbankPaymentRepositotory;
import lt.visma.starter.service.SavedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SwedbankSavedTransactionsServiceImpl implements SavedTransactionsService {
    private SwedbankPaymentRepositotory swedbankPaymentRepositotory;
    private SwedbankConfigurationProperties configurationProperties;

    @Autowired
    public SwedbankSavedTransactionsServiceImpl(SwedbankPaymentRepositotory swedbankPaymentRepositotory, SwedbankConfigurationProperties configurationProperties) {
        this.swedbankPaymentRepositotory = swedbankPaymentRepositotory;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(swedbankPaymentRepositotory.findAll());
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) throws TransactionNotFoundException {
        Optional<SwedbankPaymentTransaction> transactionOptional = swedbankPaymentRepositotory.findById(id);
        if (!transactionOptional.isPresent()) {
            throw new TransactionNotFoundException();
        }
        return Optional.of(transactionOptional.get());
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws GenericException {
        if (! (transaction instanceof SwedbankPaymentTransaction)) {
            throw new GenericException();
        }
        SwedbankPaymentTransaction swedbankTransaction = (SwedbankPaymentTransaction) transaction;
        return swedbankPaymentRepositotory.save(swedbankTransaction);
    }

    @Override
    public boolean supportsBank(String bankCode) {
        return Arrays.asList(configurationProperties.getSupportedBanks()).contains(bankCode);
    }
}
