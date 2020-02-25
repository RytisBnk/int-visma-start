package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.InvalidTransactionException;
import lt.visma.starter.model.*;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.SavedPaymentService;
import lt.visma.starter.service.BankingAPITransactionService;
import lt.visma.starter.service.factory.PaymentServiceFactory;
import lt.visma.starter.service.factory.TransactionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/payments")
public class PaymentsController {
    private PaymentServiceFactory paymentServiceFactory;
    private TransactionServiceFactory transactionServiceFactory;
    private SavedPaymentService savedPaymentService;

    @Autowired
    public PaymentsController(PaymentServiceFactory paymentServiceFactory,
                              TransactionServiceFactory transactionServiceFactory,
                              SavedPaymentService savedPaymentService) {
        this.paymentServiceFactory = paymentServiceFactory;
        this.transactionServiceFactory = transactionServiceFactory;
        this.savedPaymentService = savedPaymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getSavedPayments() {
        return new ResponseEntity<>(savedPaymentService.getPayments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id) {
        Optional<Payment> paymentOptional = savedPaymentService.getPaymentById(id);
        return paymentOptional.map(payment ->
                new ResponseEntity<>(payment, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(@RequestParam String bankCode,
                                               @RequestHeader Map<String, String> headers,
                                               @RequestBody PaymentRequest paymentRequest)
            throws BankNotSupportedException, GenericException, ApiException, InvalidTransactionException {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(bankCode);
        BankingAPITransactionService bankingAPITransactionService = transactionServiceFactory.getTransactionService(bankCode);

        PaymentResponse paymentResponse = paymentService.makePayment(paymentRequest, headers);
        Transaction transaction = bankingAPITransactionService.getTransactionById(paymentResponse.getId(), bankCode, headers);
        return new ResponseEntity<>(savedPaymentService.savePayment(transaction), HttpStatus.CREATED);
    }
}
