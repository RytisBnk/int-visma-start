package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.BankingAPITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransactionServiceFactory {
    private List<BankingAPITransactionService> bankingAPITransactionServices;

    @Autowired
    public TransactionServiceFactory(List<BankingAPITransactionService> bankingAPITransactionServices) {
        this.bankingAPITransactionServices = bankingAPITransactionServices;
    }

    public BankingAPITransactionService getTransactionService(String bankCode) throws BankNotSupportedException {
        Optional<BankingAPITransactionService> transactionServiceOptional = bankingAPITransactionServices.stream()
                .filter(transactionService -> transactionService.supportsBank(bankCode))
                .findFirst();
        if (!transactionServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return transactionServiceOptional.get();
    }
}
