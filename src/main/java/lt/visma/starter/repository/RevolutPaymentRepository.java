package lt.visma.starter.repository;

import lt.visma.starter.model.revolut.RevolutTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevolutPaymentRepository extends JpaRepository<RevolutTransaction, Long> {
}
