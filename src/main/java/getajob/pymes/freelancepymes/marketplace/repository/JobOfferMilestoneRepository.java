package getajob.pymes.freelancepymes.marketplace.repository;

import getajob.pymes.freelancepymes.marketplace.entity.OfferMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobOfferMilestoneRepository extends JpaRepository<OfferMilestone, UUID> {

    List<OfferMilestone> findByJobOfferId(UUID offerId);
}
