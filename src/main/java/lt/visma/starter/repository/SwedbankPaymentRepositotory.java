package lt.visma.starter.repository;

import lt.visma.starter.model.swedbank.SwedbankPaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SwedbankPaymentRepositotory extends JpaRepository<SwedbankPaymentTransaction, Long> {
    @Query(value = "select t from SwedbankPaymentTransaction t where t.id = ?1")
    Optional<SwedbankPaymentTransaction> findById(String id);
}
