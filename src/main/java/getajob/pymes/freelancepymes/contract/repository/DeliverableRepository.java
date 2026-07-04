package getajob.pymes.freelancepymes.contract.repository;

import getajob.pymes.freelancepymes.contract.entity.Deliverable;
import getajob.pymes.freelancepymes.contract.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliverableRepository extends JpaRepository<Deliverable, UUID> {
    Optional<Deliverable> findFirstByMilestoneOrderBySubmittedAtDesc(Milestone milestone);
}
