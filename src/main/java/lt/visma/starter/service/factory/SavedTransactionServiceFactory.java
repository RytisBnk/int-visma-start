package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.SavedTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SavedTransactionServiceFactory {
    private List<SavedTransactionsService> transactionsServices;

    @Autowired
    public SavedTransactionServiceFactory(List<SavedTransactionsService> transactionsServices) {
        this.transactionsServices = transactionsServices;
    }

    public SavedTransactionsService getSavedTransactionService(String bankCode) throws BankNotSupportedException {
        Optional<SavedTransactionsService> transactionsServiceOptional = transactionsServices.stream()
                .filter(savedTransactionsService -> savedTransactionsService.supportsBank(bankCode))
                .findFirst();
        if (!transactionsServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return transactionsServiceOptional.get();
    }
}
