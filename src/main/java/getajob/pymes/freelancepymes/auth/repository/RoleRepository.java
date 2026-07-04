package getajob.pymes.freelancepymes.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import getajob.pymes.freelancepymes.auth.entity.Role;
import getajob.pymes.freelancepymes.auth.entity.enums.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
