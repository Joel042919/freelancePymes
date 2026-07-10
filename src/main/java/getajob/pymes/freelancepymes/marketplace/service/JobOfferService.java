package getajob.pymes.freelancepymes.marketplace.service;

import getajob.pymes.freelancepymes.marketplace.dto.JobOfferRequestDTO;
import getajob.pymes.freelancepymes.marketplace.dto.JobOfferResponseDTO;
import getajob.pymes.freelancepymes.marketplace.dto.MilestoneRequestDTO;
import getajob.pymes.freelancepymes.marketplace.dto.MilestoneResponseDTO;
import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.marketplace.entity.OfferMilestone;
import getajob.pymes.freelancepymes.marketplace.enums.BudgetType;
import getajob.pymes.freelancepymes.marketplace.enums.MilestoneStatus;
import getajob.pymes.freelancepymes.marketplace.enums.ModalityType;
import getajob.pymes.freelancepymes.marketplace.enums.OfferStatus;
import getajob.pymes.freelancepymes.marketplace.enums.ProjectCategory;
import getajob.pymes.freelancepymes.marketplace.repository.JobOfferRepository;
import getajob.pymes.freelancepymes.marketplace.repository.JobOfferMilestoneRepository;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.PymeProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMilestoneRepository milestoneRepository;
    private final PymeProfileRepository pymeProfileRepository;
    private final SkillRepository skillRepository;

    public List<JobOfferResponseDTO> getAllOffers() {
        return jobOfferRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public JobOfferResponseDTO getOfferById(UUID offerId) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta de trabajo no encontrada con ID: " + offerId));
        return toResponseDTO(offer);
    }

    public List<JobOfferResponseDTO> getOffersByPyme(UUID pymeId) {
        return jobOfferRepository.findByPymeId(pymeId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public JobOfferResponseDTO createOffer(JobOfferRequestDTO dto, UUID pymeId) {
        PymeProfile pyme = pymeProfileRepository.findById(pymeId)
                .orElseThrow(() -> new IllegalArgumentException("Perfil PYME no encontrado con ID: " + pymeId));

        JobOffer offer = new JobOffer();
        offer.setPyme(pyme);
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setBudgetType(dto.getBudgetType() != null ? BudgetType.valueOf(dto.getBudgetType()) : null);
        offer.setModality(dto.getModality() != null ? ModalityType.valueOf(dto.getModality()) : null);
        offer.setProjectCategory(
                dto.getProjectCategory() != null ? ProjectCategory.valueOf(dto.getProjectCategory()) : null);
        offer.setEstimatedDays(dto.getEstimatedDays());
        offer.setTotalBudget(dto.getTotalBudget());
        offer.setMinBudget(dto.getMinBudget());
        offer.setMaxBudget(dto.getMaxBudget());
        offer.setLocation(dto.getLocation());
        offer.setStatus(OfferStatus.ABIERTA);
        offer.setPublishedAt(LocalDate.now());

        if (dto.getRequiredSkillIds() != null && !dto.getRequiredSkillIds().isEmpty()) {
            Set<Skill> skills = dto.getRequiredSkillIds().stream()
                    .map(id -> skillRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Skill no encontrada con ID: " + id)))
                    .collect(Collectors.toSet());
            offer.setRequiredSkills(skills);
        }

        if (dto.getMilestones() != null && !dto.getMilestones().isEmpty()) {
            List<OfferMilestone> milestones = dto.getMilestones().stream()
                    .map(m -> buildMilestone(m, offer))
                    .collect(Collectors.toList());
            offer.setMilestones(milestones);
        }

        JobOffer saved = jobOfferRepository.save(offer);
        return toResponseDTO(saved);
    }

    @Transactional
    public JobOfferResponseDTO updateOffer(UUID offerId, JobOfferRequestDTO dto, UUID pymeId) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta de trabajo no encontrada con ID: " + offerId));

        if (!offer.getPyme().getId().equals(pymeId)) {
            throw new SecurityException("No tienes permiso para modificar esta oferta");
        }

        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setBudgetType(dto.getBudgetType() != null ? BudgetType.valueOf(dto.getBudgetType()) : null);
        offer.setModality(dto.getModality() != null ? ModalityType.valueOf(dto.getModality()) : null);
        offer.setProjectCategory(
                dto.getProjectCategory() != null ? ProjectCategory.valueOf(dto.getProjectCategory()) : null);
        offer.setEstimatedDays(dto.getEstimatedDays());
        offer.setTotalBudget(dto.getTotalBudget());
        offer.setMinBudget(dto.getMinBudget());
        offer.setMaxBudget(dto.getMaxBudget());
        offer.setLocation(dto.getLocation());

        if (dto.getRequiredSkillIds() != null) {
            Set<Skill> skills = dto.getRequiredSkillIds().stream()
                    .map(id -> skillRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Skill no encontrada con ID: " + id)))
                    .collect(Collectors.toSet());
            offer.setRequiredSkills(skills);
        }

        if (dto.getMilestones() != null) {
            offer.getMilestones().clear();
            List<OfferMilestone> milestones = dto.getMilestones().stream()
                    .map(m -> buildMilestone(m, offer))
                    .collect(Collectors.toList());
            offer.getMilestones().addAll(milestones);
        }

        JobOffer saved = jobOfferRepository.save(offer);
        return toResponseDTO(saved);
    }

    @Transactional
    public void deleteOffer(UUID offerId, UUID pymeId) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta de trabajo no encontrada con ID: " + offerId));

        if (!offer.getPyme().getId().equals(pymeId)) {
            throw new SecurityException("No tienes permiso para eliminar esta oferta");
        }

        jobOfferRepository.delete(offer);
    }

    private OfferMilestone buildMilestone(MilestoneRequestDTO dto, JobOffer offer) {
        OfferMilestone milestone = new OfferMilestone();
        milestone.setJobOffer(offer);
        milestone.setTitle(dto.getTitle());
        milestone.setDescription(dto.getDescription());
        milestone.setAmount(dto.getAmount() != null ? java.math.BigDecimal.valueOf(dto.getAmount()) : null);
        milestone.setDueDate(dto.getDueDate());
        milestone.setStatus(MilestoneStatus.PENDIENTE_FINANCIAMIENTO);
        return milestone;
    }

    private JobOfferResponseDTO toResponseDTO(JobOffer offer) {
        Set<String> skillNames = offer.getRequiredSkills() != null
                ? offer.getRequiredSkills().stream().map(Skill::getName).collect(Collectors.toSet())
                : Collections.emptySet();

        List<MilestoneResponseDTO> milestoneDTOs = offer.getMilestones() != null
                ? offer.getMilestones().stream().map(this::toMilestoneResponse).collect(Collectors.toList())
                : Collections.emptyList();

        return JobOfferResponseDTO.builder()
                .id(offer.getId())
                .pymeId(offer.getPyme().getId())
                .companyName(offer.getPyme().getCompanyName())
                .title(offer.getTitle())
                .description(offer.getDescription())
                .budgetType(offer.getBudgetType() != null ? offer.getBudgetType().name() : null)
                .modality(offer.getModality() != null ? offer.getModality().name() : null)
                .projectCategory(offer.getProjectCategory() != null ? offer.getProjectCategory().name() : null)
                .estimatedDays(offer.getEstimatedDays())
                .totalBudget(offer.getTotalBudget())
                .minBudget(offer.getMinBudget())
                .maxBudget(offer.getMaxBudget())
                .location(offer.getLocation())
                .status(offer.getStatus().name())
                .publishedAt(offer.getPublishedAt())
                .requiredSkills(skillNames)
                .milestones(milestoneDTOs)
                .build();
    }

    private MilestoneResponseDTO toMilestoneResponse(OfferMilestone milestone) {
        return MilestoneResponseDTO.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .description(milestone.getDescription())
                .amount(milestone.getAmount() != null ? milestone.getAmount().doubleValue() : null)
                .status(milestone.getStatus().name())
                .dueDate(milestone.getDueDate())
                .build();
    }
}
