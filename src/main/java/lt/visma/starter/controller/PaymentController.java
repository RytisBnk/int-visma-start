package lt.visma.starter.controller;

import lt.visma.starter.exception.*;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.service.PaymentTransactionService;
import lt.visma.starter.service.factory.PaymentTransactionServiceFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {
    private PaymentTransactionServiceFactory paymentTransactionServiceFactory;

    public PaymentController(PaymentTransactionServiceFactory paymentTransactionServiceFactory) {
        this.paymentTransactionServiceFactory = paymentTransactionServiceFactory;
    }

    @PostMapping
    public ResponseEntity<Payment> makeNewPayment(@RequestParam String bankCode,
                                                  @RequestHeader Map<String, String> headers,
                                                  @RequestBody PaymentRequest paymentRequest)
            throws BankNotSupportedException, InvalidTransactionException, ApiException, GenericException, InvalidPaymentResponseException {
        PaymentTransactionService paymentTransactionService =
                paymentTransactionServiceFactory.getPaymentTransactionService(bankCode);

        Payment payment = paymentTransactionService.submitPayment(headers, paymentRequest, bankCode);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getPayments(@RequestParam String bankCode)
            throws BankNotSupportedException {
        PaymentTransactionService paymentTransactionService =
                paymentTransactionServiceFactory.getPaymentTransactionService(bankCode);

        List<Payment> payments = paymentTransactionService.getPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id,
                                                  @RequestParam String bankCode)
            throws BankNotSupportedException {
        PaymentTransactionService paymentTransactionService =
                paymentTransactionServiceFactory.getPaymentTransactionService(bankCode);

        Payment payment = paymentTransactionService.getPaymentById(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
