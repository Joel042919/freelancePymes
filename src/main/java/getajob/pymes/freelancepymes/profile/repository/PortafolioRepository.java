package getajob.pymes.freelancepymes.profile.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import getajob.pymes.freelancepymes.profile.entity.Portfolio;
import org.springframework.stereotype.Repository;

@Repository
public interface PortafolioRepository extends JpaRepository<Portfolio, UUID>{
    
}
