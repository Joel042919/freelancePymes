package getajob.pymes.freelancepymes.profile.entity;

import java.util.Set;
import java.util.UUID;

import getajob.pymes.freelancepymes.auth.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="freelancer_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FreelancerProfile {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId //El ID de esta tabla será el mismo ID del User
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="first_name")
    private String firstname;

    @Column(name="last_name")
    private String lastName;

    @Column(columnDefinition="TEXT")
    private String bio;

    @ManyToMany
    @JoinTable(
        name="freelancer_skills",
        joinColumns = @JoinColumn(name="freelancer_id"),
        inverseJoinColumns = @JoinColumn(name="skill_id")
    )
    private Set<Skill> skills;
}
