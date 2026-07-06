package getajob.pymes.freelancepymes.payment.repository;

import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByMilestone(Milestone milestone);
}
