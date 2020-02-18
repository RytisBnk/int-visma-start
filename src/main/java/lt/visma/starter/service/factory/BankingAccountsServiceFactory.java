package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.BankingAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BankingAccountsServiceFactory {
    private List<BankingAccountsService> bankingAccountsServices;

    @Autowired
    public BankingAccountsServiceFactory(List<BankingAccountsService> bankingAccountsServices) {
        this.bankingAccountsServices = bankingAccountsServices;
    }

    public BankingAccountsService getBankingAccountsService(String bankCode) throws BankNotSupportedException {
        Optional<BankingAccountsService> bankingAccountsServiceOptional = bankingAccountsServices.stream()
                .filter(bankingAccountsService -> bankingAccountsService.supportsBank(bankCode))
                .findFirst();
        if (!bankingAccountsServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return bankingAccountsServiceOptional.get();
    }
}
