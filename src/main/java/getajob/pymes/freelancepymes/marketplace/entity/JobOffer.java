package getajob.pymes.freelancepymes.marketplace.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import getajob.pymes.freelancepymes.marketplace.enums.BudgetType;
import getajob.pymes.freelancepymes.marketplace.enums.ModalityType;
import getajob.pymes.freelancepymes.marketplace.enums.OfferStatus;
import getajob.pymes.freelancepymes.marketplace.enums.ProjectCategory;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "job_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pyme_id")
    private PymeProfile pyme;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "budget_type")
    private BudgetType budgetType;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "modality")
    private ModalityType modality;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "project_category")
    private ProjectCategory projectCategory;

    @Column(name = "estimated_days")
    private Integer estimatedDays;

    @Column(name = "total_budget")
    private Double totalBudget;

    @Column(name = "min_budget")
    private Double minBudget;

    @Column(name = "max_budget")
    private Double maxBudget;

    private String location;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private OfferStatus status;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @ManyToMany
    @JoinTable(
        name = "offer_skills",
        joinColumns = @JoinColumn(name = "offer_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> requiredSkills;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OfferMilestone> milestones = new ArrayList<>();
}
