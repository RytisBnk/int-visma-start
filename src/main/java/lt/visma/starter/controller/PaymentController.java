package lt.visma.starter.controller;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.model.PaymentRequest;
import lt.visma.starter.model.entity.PaymentQueueEntry;
import lt.visma.starter.service.PaymentQueueService;
import lt.visma.starter.service.factory.PaymentQueueServiceFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/jms/payments")
public class PaymentController {
    private PaymentQueueServiceFactory paymentQueueServiceFactory;

    public PaymentController(PaymentQueueServiceFactory paymentQueueServiceFactory) {
        this.paymentQueueServiceFactory = paymentQueueServiceFactory;
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
}
