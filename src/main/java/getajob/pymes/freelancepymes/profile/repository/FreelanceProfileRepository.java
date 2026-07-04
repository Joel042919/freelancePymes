package getajob.pymes.freelancepymes.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;

public interface FreelanceProfileRepository extends JpaRepository<FreelancerProfile, UUID> {
    
}
