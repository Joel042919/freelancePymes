package getajob.pymes.freelancepymes.marketplace.entity;

import java.util.Set;
import java.util.UUID;

import getajob.pymes.freelancepymes.marketplace.enums.BudgetType;
import getajob.pymes.freelancepymes.marketplace.enums.OfferStatus;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="job_offers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pyme_id")
    private PymeProfile pyme;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "budget_type")
    private BudgetType budgetType; // Crea este Enum en una carpeta enums

    @Column(name = "total_budget")
    private Double totalBudget;

    @Enumerated(EnumType.STRING)
    private OfferStatus status; // Crea este Enum

    @ManyToMany
    @JoinTable(
        name = "offer_skills",
        joinColumns = @JoinColumn(name = "offer_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> requiredSkills;
    
}
