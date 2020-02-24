package lt.visma.starter.repository;

import lt.visma.starter.model.entity.PaymentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentSubmissionRepository extends JpaRepository<PaymentSubmission, Long> {
}
