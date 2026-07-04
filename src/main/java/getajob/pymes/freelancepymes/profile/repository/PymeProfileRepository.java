package getajob.pymes.freelancepymes.profile.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;

@Repository
public interface PymeProfileRepository extends JpaRepository<PymeProfile, UUID> {
}
