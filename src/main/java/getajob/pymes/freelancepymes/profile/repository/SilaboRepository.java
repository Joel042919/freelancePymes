package getajob.pymes.freelancepymes.profile.repository;

import getajob.pymes.freelancepymes.profile.entity.Silabo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SilaboRepository extends JpaRepository<Silabo, Integer> {
    List<Silabo> findBySkillId(Integer skillId);
}
