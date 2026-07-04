package getajob.pymes.freelancepymes.dispute.entity;

import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.dispute.enums.DisputeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "disputes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relación 1:1 con milestone
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id", unique = true)
    private Milestone milestone;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ai_resolution_report")
    private Map<String, Object> aiResolutionReport;

    @Enumerated(EnumType.STRING)
    private DisputeStatus status;
}
