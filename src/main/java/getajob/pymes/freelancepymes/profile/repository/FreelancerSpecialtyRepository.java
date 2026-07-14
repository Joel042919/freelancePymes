package getajob.pymes.freelancepymes.profile.repository;

import getajob.pymes.freelancepymes.profile.entity.FreelancerSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface FreelancerSpecialtyRepository extends JpaRepository<FreelancerSpecialty, Integer> {
    List<FreelancerSpecialty> findByFreelancerProfile_Id(UUID freelancerId);
}
