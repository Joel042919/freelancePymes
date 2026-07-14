package getajob.pymes.freelancepymes.contract.entity;

import java.util.UUID;

import getajob.pymes.freelancepymes.contract.enums.ContractStatus;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
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
@Table(name = "contracts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relación 1:1. Un contrato pertenece a una sola postulación.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", unique = true)
    private Application application;

    // TEXT en vez de varchar(255): la firma puede ser una imagen PNG en base64 (varios KB).
    @Column(name = "digital_signature_pyme", columnDefinition = "TEXT")
    private String digitalSignaturePyme;

    @Column(name = "digital_signature_freelancer", columnDefinition = "TEXT")
    private String digitalSignatureFreelancer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ContractStatus status;
}
