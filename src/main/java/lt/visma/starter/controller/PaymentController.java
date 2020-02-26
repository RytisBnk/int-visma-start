package lt.visma.starter.controller;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.Payment;
import lt.visma.starter.model.entity.PaymentQueueEntry;
import lt.visma.starter.service.PaymentQueueService;
import lt.visma.starter.service.PaymentService;
import lt.visma.starter.service.factory.PaymentQueueServiceFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {
    private PaymentQueueServiceFactory paymentQueueServiceFactory;
    private PaymentService paymentService;

    public PaymentController(PaymentQueueServiceFactory paymentQueueServiceFactory,
                             PaymentService paymentService) {
        this.paymentQueueServiceFactory = paymentQueueServiceFactory;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentQueueEntry> putPaymentToProcessing(@RequestParam String bankCode,
                                                                    @RequestHeader Map<String, String> authParams,
                                                                    @RequestBody PaymentRequest paymentRequest)
            throws BankNotSupportedException {
        PaymentQueueService paymentQueueService = paymentQueueServiceFactory.getPaymentQueueService(bankCode);
        PaymentQueueEntry paymentQueueEntry = paymentQueueService.submitPaymentToQueue(bankCode, authParams, paymentRequest);

        return new ResponseEntity<>(paymentQueueEntry, HttpStatus.OK);
    }

    @GetMapping("/queue/{id}")
    public ResponseEntity<PaymentQueueEntry> getPaymentQueueStatus(@PathVariable  String id,
                                                                   @RequestParam String bankCode)
            throws BankNotSupportedException {
        PaymentQueueService paymentQueueService = paymentQueueServiceFactory.getPaymentQueueService(bankCode);
        PaymentQueueEntry queueEntry = paymentQueueService.getQueueEntryById(id);
        return new ResponseEntity<>(queueEntry, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id) {
        Payment payment = paymentService.getPaymentById(id).orElse(null);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
