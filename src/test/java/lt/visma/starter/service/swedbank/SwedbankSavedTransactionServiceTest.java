package lt.visma.starter.service.swedbank;

import lt.visma.starter.configuration.SwedbankConfigurationProperties;
import lt.visma.starter.exception.TransactionNotFoundException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.model.swedbank.entity.SwedbankPaymentTransaction;
import lt.visma.starter.repository.SwedbankPaymentRepositotory;
import lt.visma.starter.service.impl.swedbank.SwedbankSavedTransactionsServiceImpl;
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
public class SwedbankSavedTransactionServiceTest {
    @Mock
    private SwedbankPaymentRepositotory swedbankPaymentRepositotory;

    @Mock
    private SwedbankConfigurationProperties configurationProperties;

    @InjectMocks
    private SwedbankSavedTransactionsServiceImpl savedTransactionsService;

    private final String TARGET_TRANSACTION_ID = "3-3-3-3";
    private final String ALTERNATIVE_TRANSACTION_ID_1 = "1-1-1-1";
    private final String ALTERNATIVE_TRANSACTION_ID_2 = "2-2-2-2";

    @Test
    public void getTransactionById_IdDoesntExist_throwsTransactionNotFoundException() {
        Optional<SwedbankPaymentTransaction> transactionOptional = Optional.empty();

        when(swedbankPaymentRepositotory.findById(TARGET_TRANSACTION_ID)).thenReturn(transactionOptional);

        assertThrows(TransactionNotFoundException.class, () -> savedTransactionsService.getTransactionById(TARGET_TRANSACTION_ID));
    }

    @Test
    public void getTransactionById_ExistingIdProvided_ReturnsTransactionObject() throws TransactionNotFoundException {
        SwedbankPaymentTransaction expected = new SwedbankPaymentTransaction();
        expected.setId(TARGET_TRANSACTION_ID);

        when(swedbankPaymentRepositotory.findById(TARGET_TRANSACTION_ID)).thenReturn(Optional.of(expected));

        Optional<Transaction> actual = savedTransactionsService.getTransactionById(TARGET_TRANSACTION_ID);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void getTransactions_TransactionsPresent_ReturnsAllTransactions() {
        List<SwedbankPaymentTransaction> expectedTransactions = new ArrayList<>();

        SwedbankPaymentTransaction transaction1 = new SwedbankPaymentTransaction();
        transaction1.setId(ALTERNATIVE_TRANSACTION_ID_1);

        SwedbankPaymentTransaction transaction2 = new SwedbankPaymentTransaction();
        transaction2.setId(ALTERNATIVE_TRANSACTION_ID_2);

        expectedTransactions.add(transaction1);
        expectedTransactions.add(transaction2);

        when(swedbankPaymentRepositotory.findAll()).thenReturn(expectedTransactions);

        List<Transaction> actualTransactions = savedTransactionsService.getAllTransactions();

        assertEquals(expectedTransactions, actualTransactions);
    }
}
