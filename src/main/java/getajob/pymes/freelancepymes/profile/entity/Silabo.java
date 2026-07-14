package getajob.pymes.freelancepymes.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="silabo")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class Silabo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false, length = 60)
    private String punto;

    @Column(columnDefinition = "TEXT")
    private String description;
}
