package getajob.pymes.freelancepymes.marketplace.entity;

import java.util.UUID;

import getajob.pymes.freelancepymes.marketplace.enums.ApplicationStatus;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private JobOffer offer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id")
    private FreelancerProfile freelancer;

    @Column(name = "proposed_amount")
    private Double proposedAmount;

    @Column(name = "estimated_days")
    private Integer estimatedDays;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "application_status")
    private ApplicationStatus status;
}

