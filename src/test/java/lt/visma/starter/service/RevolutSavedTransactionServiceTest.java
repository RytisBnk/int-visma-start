package lt.visma.starter.service;

import lt.visma.starter.configuration.RevolutConfigurationProperties;
import lt.visma.starter.exception.TransactionNotFoundException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import lt.visma.starter.repository.RevolutPaymentRepository;
import lt.visma.starter.service.impl.revolut.RevolutSavedTransactionsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RevolutSavedTransactionServiceTest {
    @Mock
    private RevolutPaymentRepository revolutPaymentRepository;

    @Mock
    private RevolutConfigurationProperties configurationProperties;

    @InjectMocks
    private RevolutSavedTransactionsServiceImpl savedTransactionsService;

    private final String TARGET_TRANSACTION_ID = "1-1-1-1";
    private final String ALTERNATIVE_TRANSACTION_ID = "2-2-2-2";

    @Test
    public void getTransactionById_IdDoesntExist_ThrowsTransactionNotFoundException() {
        Optional<RevolutTransaction> transactionOptional = Optional.empty();

        when(revolutPaymentRepository.findById(TARGET_TRANSACTION_ID)).thenReturn(transactionOptional);

        assertThrows(TransactionNotFoundException.class, () -> savedTransactionsService.getTransactionById(TARGET_TRANSACTION_ID));
    }

    @Test
    public void getTransactionById_ExistingIdProvided_ReturnsTransactionObject() throws TransactionNotFoundException {
        RevolutTransaction expected = new RevolutTransaction();
        expected.setId(TARGET_TRANSACTION_ID);

        when(revolutPaymentRepository.findById(TARGET_TRANSACTION_ID)).thenReturn(Optional.of(expected));

        Optional<Transaction> actualOptional = savedTransactionsService.getTransactionById(TARGET_TRANSACTION_ID);

        assertEquals(Optional.of(expected), actualOptional);
    }

    @Test
    public void getTransactions_TransactionsPresent_returnsTransactions() {
        RevolutTransaction transaction1 = new RevolutTransaction();
        transaction1.setId(TARGET_TRANSACTION_ID);

        RevolutTransaction transaction2 = new RevolutTransaction();
        transaction2.setId(ALTERNATIVE_TRANSACTION_ID);

        List<RevolutTransaction> expectedtTransactions = new ArrayList<>();
        expectedtTransactions.add(transaction1);
        expectedtTransactions.add(transaction2);

        when(revolutPaymentRepository.findAll()).thenReturn(expectedtTransactions);

        List<Transaction> actualTransactions = savedTransactionsService.getAllTransactions();

        assertNotNull(expectedtTransactions);
        assertEquals(expectedtTransactions, actualTransactions);
    }
}
