package getajob.pymes.freelancepymes.contract.repository;

import getajob.pymes.freelancepymes.contract.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
}
