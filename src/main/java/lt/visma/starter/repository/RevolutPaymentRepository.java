package lt.visma.starter.repository;

import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevolutPaymentRepository extends JpaRepository<RevolutTransaction, String> {
}
