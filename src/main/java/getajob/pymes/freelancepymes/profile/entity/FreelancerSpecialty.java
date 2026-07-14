package getajob.pymes.freelancepymes.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="freelancer_specialties")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class FreelancerSpecialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerProfile freelancerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt = LocalDateTime.now();
}
