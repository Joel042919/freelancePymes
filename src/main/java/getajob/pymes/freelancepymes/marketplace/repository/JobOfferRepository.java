package getajob.pymes.freelancepymes.marketplace.repository;

import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.marketplace.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, UUID> {
    List<JobOffer> findByStatus(OfferStatus status);

    List<JobOffer> findByPymeId(UUID pymeId);
}
