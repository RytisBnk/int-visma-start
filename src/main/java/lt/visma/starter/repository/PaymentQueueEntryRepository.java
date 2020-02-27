package lt.visma.starter.repository;

import lt.visma.starter.model.entity.PaymentQueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentQueueEntryRepository extends JpaRepository<PaymentQueueEntry, String> {
}
