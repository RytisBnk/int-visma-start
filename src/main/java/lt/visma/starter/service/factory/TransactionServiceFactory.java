package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransactionServiceFactory {
    private List<TransactionService> transactionServices;

    @Autowired
    public TransactionServiceFactory(List<TransactionService> transactionServices) {
        this.transactionServices = transactionServices;
    }

    public TransactionService getTransactionService(String bankCode) throws BankNotSupportedException {
        Optional<TransactionService> transactionServiceOptional = transactionServices.stream()
                .filter(transactionService -> transactionService.supportsBank(bankCode))
                .findFirst();
        if (!transactionServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return transactionServiceOptional.get();
    }
}
