package getajob.pymes.freelancepymes.marketplace.repository;

import getajob.pymes.freelancepymes.marketplace.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    boolean existsByOfferIdAndFreelancerId(UUID offerId, UUID freelancerId);
    List<Application> findByOffer_Pyme_User_Email(String email);
}
