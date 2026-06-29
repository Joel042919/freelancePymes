package getajob.pymes.freelancepymes.profile.entity;

import java.util.UUID;

import getajob.pymes.freelancepymes.auth.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="pyme_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PymeProfile {
    @Id
    private UUID id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "company_name")
    private String companyName;

    private String industry;

    @Column(name = "verification_badge")
    private Boolean verificationBadge = false;

    @Column(name = "reputation_score")
    private Double reputationScore;
}
