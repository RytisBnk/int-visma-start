package lt.visma.starter.controller;

import lt.visma.starter.exception.ApiException;
import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.exception.GenericException;
import lt.visma.starter.exception.OperationNotSupportedException;
import lt.visma.starter.model.Transaction;
import lt.visma.starter.service.AuthenticationService;
import lt.visma.starter.service.TransactionService;
import lt.visma.starter.service.factory.AuthenticationServiceFactory;
import lt.visma.starter.service.factory.TransactionServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionsController {
    private AuthenticationServiceFactory authenticationServiceFactory;
    private TransactionServiceFactory transactionServiceFactory;

    @Autowired
    public TransactionsController(AuthenticationServiceFactory authenticationServiceFactory,
                                  TransactionServiceFactory transactionServiceFactory) {
        this.authenticationServiceFactory = authenticationServiceFactory;
        this.transactionServiceFactory = transactionServiceFactory;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam("bankCode") String bankCode,
                                                             @RequestParam("from") String from,
                                                             @RequestParam("to") String to,
                                                             @RequestHeader Map<String, String> headers)
            throws BankNotSupportedException, GenericException, ApiException, OperationNotSupportedException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(headers);
        return new ResponseEntity<>(transactionService.getTransactions(accessToken, from, to), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") String id,
                                                          @RequestParam("bankCode") String bankCode,
                                                          @RequestHeader Map<String, String> headers)
            throws BankNotSupportedException, GenericException, ApiException {
        AuthenticationService authenticationService = authenticationServiceFactory.getAuthenticationService(bankCode);
        TransactionService transactionService = transactionServiceFactory.getTransactionService(bankCode);
        String accessToken = authenticationService.getAccessToken(headers);
        return new ResponseEntity<>(transactionService.getTransactionById(accessToken, id, bankCode), HttpStatus.OK);
    }
}
