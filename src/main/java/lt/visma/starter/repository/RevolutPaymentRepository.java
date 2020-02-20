package lt.visma.starter.repository;

import lt.visma.starter.model.revolut.entity.RevolutTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RevolutPaymentRepository extends JpaRepository<RevolutTransaction, Long> {
    @Query(value = "select t from RevolutTransaction t where t.id = ?1")
    Optional<RevolutTransaction> findById(String id);
}
