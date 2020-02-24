package lt.visma.starter.service.swedbank;

import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.Payment;
import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import lt.visma.starter.model.swedbank.entity.AccountIBAN;
import lt.visma.starter.model.swedbank.entity.PaymentAmount;
import lt.visma.starter.model.swedbank.entity.RemittanceInformation;
import lt.visma.starter.model.swedbank.entity.SwedbankPaymentTransaction;
import lt.visma.starter.service.impl.swedbank.SwedbankPaymentMapperServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class SwedbankPaymentMapperServiceTest {
    private SwedbankPaymentMapperServiceImpl swedbankPaymentMapperService;

    private final String TRANSACTION_ID = "1-1-1-1";
    private final String CREDITOR_ACCOUNT = "LT11112222333344";
    private final String DEBTOR_ACCOUNT = "LT22223333444455";
    private final String AMOUNT = "15.30";
    private final String CURRENCY = "EUR";
    private final String BANK_NAME = "Swedbank";
    private final String REFERENCE = "Invoice payment #66";

    @Before
    public void setUp() throws Exception {
        swedbankPaymentMapperService = new SwedbankPaymentMapperServiceImpl();
    }

    @Test
    public void mapToPersistentPaymentObject_SwedbankPaymentTransactionprovided_ReturnsCorrectPaymentObject()
            throws InvalidTransactionException {
        SwedbankPaymentTransaction swedbankPaymentTransaction = new SwedbankPaymentTransaction();
        swedbankPaymentTransaction.setId(TRANSACTION_ID);
        swedbankPaymentTransaction.setCreditorAccount(new AccountIBAN(CREDITOR_ACCOUNT));
        swedbankPaymentTransaction.setDebtorAccount(new AccountIBAN(DEBTOR_ACCOUNT));
        swedbankPaymentTransaction.setInstructedAmount(new PaymentAmount(AMOUNT, CURRENCY));
        swedbankPaymentTransaction.setRemittanceInformationStructured(new RemittanceInformation(REFERENCE));

        Payment payment = swedbankPaymentMapperService.mapToPersistentPaymentObject(swedbankPaymentTransaction);

        assertNotNull(payment);
        assertEquals(TRANSACTION_ID, payment.getId());
        assertEquals(CREDITOR_ACCOUNT, payment.getCreditorAccount());
        assertEquals(DEBTOR_ACCOUNT, payment.getDebtorAccount());
        assertEquals(Double.parseDouble(AMOUNT), payment.getAmount());
        assertEquals(CURRENCY, payment.getCurrency());
        assertEquals(REFERENCE, payment.getReference());
        assertEquals(BANK_NAME, payment.getBankName());
    }

    @Test
    public void mapToPersistentPaymentObject_BadTypeProvided_ThrowsInvalidTransactionException() {
        RevolutTransaction transaction = new RevolutTransaction();

        assertThrows(InvalidTransactionException.class,
                () -> swedbankPaymentMapperService.mapToPersistentPaymentObject(transaction));
    }
}
