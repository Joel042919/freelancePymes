package getajob.pymes.freelancepymes.ai.entity;

import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="assessments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="freelancer_id")
    private FreelancerProfile freelancer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="skill_id")
    private Skill skill;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "quiz_data", columnDefinition = "jsonb")
    private Map<String, Object> quizData; 

    private Double score;
    private Boolean passed;
}
