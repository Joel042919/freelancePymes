package getajob.pymes.freelancepymes.marketplace.repository;

import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, UUID> {
}
