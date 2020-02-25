package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsentServiceFactory {
    private final List<ConsentService> consentServices;

    @Autowired
    public ConsentServiceFactory(List<ConsentService> consentServices) {
        this.consentServices = consentServices;
    }

    public ConsentService getConsentService(String bankCode) throws BankNotSupportedException {
        Optional<ConsentService> consentServiceOptional = consentServices.stream()
                .filter(consentService -> consentService.supportsBank(bankCode))
                .findFirst();
        if (! consentServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return consentServiceOptional.get();
    }
}
