package getajob.pymes.freelancepymes.ai.repository;

import getajob.pymes.freelancepymes.ai.entity.Assessment;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByFreelancerAndPassed(FreelancerProfile freelancer, Boolean passed);
}
