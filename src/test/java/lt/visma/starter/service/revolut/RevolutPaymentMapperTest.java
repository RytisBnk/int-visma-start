package lt.visma.starter.service.revolut;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.model.revolut.Counterparty;
import lt.visma.starter.model.revolut.RevolutTransaction;
import lt.visma.starter.model.revolut.TransactionLeg;
import lt.visma.starter.model.swedbank.SwedbankPaymentTransaction;
import lt.visma.starter.mapper.impl.revolut.RevolutPaymentMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class RevolutPaymentMapperTest {
    private RevolutPaymentMapper revolutPaymentMapperService;

    private final String TRANSACTION_ID = "1-1-1-1";
    private final String CREDITOR_ID = "2-2-2-2";
    private final String DEBTOR_ID = "3-3-3-3";
    private final double AMOUNT = 15.3;
    private final String CURRENCY = "GBP";
    private final String BANK_NAME = "Revolut";
    private final String REFERENCE = "Invoice payment #66";

    @Before
    public void setUp() throws Exception {
        revolutPaymentMapperService = new RevolutPaymentMapper();
    }

    @Test
    public void mapToPersistentPaymentObject_RevolutTransactionProvided_ReturnsPaymentObject()
            throws InvalidTransactionException {
        RevolutTransaction revolutTransaction = new RevolutTransaction();
        revolutTransaction.setId(TRANSACTION_ID);

        TransactionLeg transactionLeg = new TransactionLeg();
        transactionLeg.setAccountId(DEBTOR_ID);
        transactionLeg.setCounterparty(new Counterparty(CREDITOR_ID));
        transactionLeg.setAmount(AMOUNT);
        transactionLeg.setCurrency(CURRENCY);
        transactionLeg.setDescription(REFERENCE);

        List<TransactionLeg> legs = new ArrayList<>();
        legs.add(transactionLeg);
        revolutTransaction.setLegs(legs);

        Payment payment = revolutPaymentMapperService.mapToPersistentPaymentObject(revolutTransaction);

        assertNotNull(payment);
        assertEquals(TRANSACTION_ID, payment.getId());
        assertEquals(CREDITOR_ID, payment.getCreditorAccount());
        assertEquals(DEBTOR_ID, payment.getDebtorAccount());
        assertEquals(AMOUNT, payment.getAmount());
        assertEquals(CURRENCY, payment.getCurrency());
        assertEquals(REFERENCE, payment.getReference());
        assertEquals(BANK_NAME, payment.getBankName());
    }

    @Test
    public void mapToPersistentPaymentObject_BadTransactionTypeProvided_ThrowsInvalidTransactionException() {
        SwedbankPaymentTransaction swedbankPaymentTransaction = new SwedbankPaymentTransaction();

        assertThrows(InvalidTransactionException.class,
                () -> revolutPaymentMapperService.mapToPersistentPaymentObject(swedbankPaymentTransaction));
    }
}
