package getajob.pymes.freelancepymes.profile.repository;

import getajob.pymes.freelancepymes.profile.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    Specialty findByName(String name);
}
