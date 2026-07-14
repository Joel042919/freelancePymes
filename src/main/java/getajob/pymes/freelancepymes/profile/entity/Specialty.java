package getajob.pymes.freelancepymes.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="specialties")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
        name = "specialty_skills",
        joinColumns = @JoinColumn(name = "specialty_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;
}
