package lt.visma.starter.service.factory;

import lt.visma.starter.exception.BankNotSupportedException;
import lt.visma.starter.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AuthenticationServiceFactory {
    private List<AuthenticationService> authenticationServices;

    @Autowired
    public AuthenticationServiceFactory(List<AuthenticationService> authenticationServices) {
        this.authenticationServices = authenticationServices;
    }

    public AuthenticationService getAuthenticationService(String bankCode) throws BankNotSupportedException {
        Optional<AuthenticationService> authenticationServiceOptional = authenticationServices.stream()
                .filter(authenticationService -> authenticationService.supportsBank(bankCode))
                .findFirst();
        if (!authenticationServiceOptional.isPresent()) {
            throw new BankNotSupportedException("Bank " + bankCode + " is not supported");
        }
        return authenticationServiceOptional.get();
    }
}
