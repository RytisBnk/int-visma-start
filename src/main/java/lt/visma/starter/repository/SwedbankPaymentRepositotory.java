package lt.visma.starter.repository;

import lt.visma.starter.model.swedbank.entity.SwedbankPaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwedbankPaymentRepositotory extends JpaRepository<SwedbankPaymentTransaction, String> {
}
