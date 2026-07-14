package getajob.pymes.freelancepymes.config;

import getajob.pymes.freelancepymes.ai.entity.Assessment;
import getajob.pymes.freelancepymes.ai.repository.AssessmentRepository;
import getajob.pymes.freelancepymes.auth.entity.Role;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.entity.enums.RoleName;
import getajob.pymes.freelancepymes.auth.repository.RoleRepository;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.contract.entity.Contract;
import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.contract.enums.ContractStatus;
import getajob.pymes.freelancepymes.contract.enums.MilestoneStatus;
import getajob.pymes.freelancepymes.contract.repository.ContractRepository;
import getajob.pymes.freelancepymes.contract.repository.MilestoneRepository;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.marketplace.enums.ApplicationStatus;
import getajob.pymes.freelancepymes.marketplace.enums.BudgetType;
import getajob.pymes.freelancepymes.marketplace.enums.OfferStatus;
import getajob.pymes.freelancepymes.marketplace.repository.ApplicationRepository;
import getajob.pymes.freelancepymes.marketplace.repository.JobOfferRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.entity.Specialty;
import getajob.pymes.freelancepymes.profile.entity.Silabo;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.PymeProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;
import getajob.pymes.freelancepymes.profile.repository.SpecialtyRepository;
import getajob.pymes.freelancepymes.profile.repository.SilaboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final PymeProfileRepository pymeProfileRepository;
    private final AssessmentRepository assessmentRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ApplicationRepository applicationRepository;
    private final ContractRepository contractRepository;
    private final MilestoneRepository milestoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final SpecialtyRepository specialtyRepository;
    private final SilaboRepository silaboRepository;
    private final getajob.pymes.freelancepymes.profile.repository.FreelancerSpecialtyRepository freelancerSpecialtyRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 0. Ampliar columnas de firma digital: eran varchar(255) (pensadas para un texto corto
        // de ejemplo), pero la firma dibujada a mano se guarda como imagen PNG en base64, que
        // supera ampliamente ese límite. Hibernate con ddl-auto=update no altera columnas ya
        // existentes, así que se hace manualmente aquí. Es una operación idempotente y segura
        // (solo amplía el tipo, no borra ni trunca datos existentes).
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE contracts ALTER COLUMN digital_signature_pyme TYPE TEXT");
            statement.execute("ALTER TABLE contracts ALTER COLUMN digital_signature_freelancer TYPE TEXT");
        } catch (Exception e) {
            // Ignore in H2 during tests
        }

        // 1. Seed Roles
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }

        // 2. Seed Skills
        Skill java = getOrSeedSkill("Java", "Backend");
        Skill spring = getOrSeedSkill("Spring Boot", "Backend");
        Skill react = getOrSeedSkill("React", "Frontend");
        Skill docker = getOrSeedSkill("Docker", "DevOps");

        // Seed Specialties
        Specialty backendSpecialty = specialtyRepository.findByName("Desarrollador Backend");
        if (backendSpecialty == null) {
            backendSpecialty = new Specialty(null, "Desarrollador Backend", "Especialista en servidores y APIs", Set.of(java, spring));
            specialtyRepository.save(backendSpecialty);
        }

        Specialty frontendSpecialty = specialtyRepository.findByName("Desarrollador Frontend");
        if (frontendSpecialty == null) {
            frontendSpecialty = new Specialty(null, "Desarrollador Frontend", "Especialista en UI/UX", Set.of(react));
            specialtyRepository.save(frontendSpecialty);
        }

        Specialty devopsSpecialty = specialtyRepository.findByName("DevOps Engineer");
        if (devopsSpecialty == null) {
            devopsSpecialty = new Specialty(null, "DevOps Engineer", "Infraestructura y contenedores", Set.of(docker));
            specialtyRepository.save(devopsSpecialty);
        }

        // Seed Silabos
        if (silaboRepository.findBySkillId(react.getId()).isEmpty()) {
            silaboRepository.save(new Silabo(null, react, "Componentes React", "Uso de estado, hooks y ciclo de vida"));
            silaboRepository.save(new Silabo(null, react, "React Router", "Navegación y rutas"));
            silaboRepository.save(new Silabo(null, react, "Gestión de Estado", "Redux o Context API"));
        }
        if (silaboRepository.findBySkillId(spring.getId()).isEmpty()) {
            silaboRepository.save(new Silabo(null, spring, "Spring Core", "Inyección de dependencias y beans"));
            silaboRepository.save(new Silabo(null, spring, "Spring Boot Web", "Creación de APIs REST"));
            silaboRepository.save(new Silabo(null, spring, "Spring Data JPA", "Mapeo objeto-relacional"));
        }
        if (silaboRepository.findBySkillId(java.getId()).isEmpty()) {
            silaboRepository.save(new Silabo(null, java, "Programación Orientada a Objetos", "Clases, herencia, polimorfismo en Java"));
            silaboRepository.save(new Silabo(null, java, "Colecciones y Streams", "Listas, Mapas, Filter, Map, Reduce"));
            silaboRepository.save(new Silabo(null, java, "Concurrencia", "Hilos, CompletableFuture, Executors"));
        }
        if (silaboRepository.findBySkillId(docker.getId()).isEmpty()) {
            silaboRepository.save(new Silabo(null, docker, "Fundamentos de Contenedores", "Diferencia entre VM y Docker"));
            silaboRepository.save(new Silabo(null, docker, "Imágenes y Dockerfile", "Creación de imágenes y optimización"));
            silaboRepository.save(new Silabo(null, docker, "Docker Compose", "Orquestación multi-contenedor local"));
        }

        // 3. Seed Freelancer User and Profile
        String freelancerEmail = "freelancer@example.com";
        FreelancerProfile savedFreelancer = null;
        if (!userRepository.existsByEmail(freelancerEmail)) {
            Role role = roleRepository.findByName(RoleName.FREELANCER)
                    .orElseThrow(() -> new IllegalStateException("Role FREELANCER not found"));

            User user = User.builder()
                    .email(freelancerEmail)
                    .password(passwordEncoder.encode("SecurePassword123!"))
                    .role(role)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);

            FreelancerProfile freelancerProfile = new FreelancerProfile();
            freelancerProfile.setUser(savedUser);
            freelancerProfile.setFirstname("John");
            freelancerProfile.setLastName("Doe");
            freelancerProfile.setBio("Experienced Java developer");
            freelancerProfile.setSkills(Set.of(java, spring));
            savedFreelancer = freelanceProfileRepository.save(freelancerProfile);
            savedFreelancer = freelanceProfileRepository.save(freelancerProfile);
        } else {
            User user = userRepository.findByEmail(freelancerEmail).orElse(null);
            if (user != null) {
                savedFreelancer = freelanceProfileRepository.findById(user.getId()).orElse(null);
            }
        }

        if (savedFreelancer != null) {
            // Remove previous assessments to reset state
            assessmentRepository.deleteAll(assessmentRepository.findByFreelancer(savedFreelancer));

            // Force Skill Gap: Clear all skills and only add Java and Spring
            savedFreelancer.getSkills().clear();
            savedFreelancer.getSkills().add(java);
            savedFreelancer.getSkills().add(spring);
            freelanceProfileRepository.save(savedFreelancer);

            // Assign Specialties
            if (freelancerSpecialtyRepository.findByFreelancerProfile_Id(savedFreelancer.getId()).isEmpty()) {
                freelancerSpecialtyRepository.save(new getajob.pymes.freelancepymes.profile.entity.FreelancerSpecialty(
                    null, savedFreelancer, backendSpecialty, null, LocalDateTime.now()
                ));
                freelancerSpecialtyRepository.save(new getajob.pymes.freelancepymes.profile.entity.FreelancerSpecialty(
                    null, savedFreelancer, devopsSpecialty, null, LocalDateTime.now()
                ));
            }
        }

        // 4. Seed Pyme User and Profile
        String pymeEmail = "pyme@example.com";
        PymeProfile savedPyme = null;
        if (!userRepository.existsByEmail(pymeEmail)) {
            Role role = roleRepository.findByName(RoleName.PYME)
                    .orElseThrow(() -> new IllegalStateException("Role PYME not found"));

            User user = User.builder()
                    .email(pymeEmail)
                    .password(passwordEncoder.encode("SecurePassword123!"))
                    .role(role)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);

            PymeProfile pymeProfile = new PymeProfile();
            pymeProfile.setUser(savedUser);
            pymeProfile.setCompanyName("Acme Software");
            pymeProfile.setIndustry("Information Technology");
            pymeProfile.setVerificationBadge(true);
            pymeProfile.setReputationScore(4.8);
            savedPyme = pymeProfileRepository.save(pymeProfile);
        } else {
            User user = userRepository.findByEmail(pymeEmail).orElse(null);
            if (user != null) {
                savedPyme = pymeProfileRepository.findById(user.getId()).orElse(null);
            }
        }

        // 5. Seed Job Offers linked to Pyme
        JobOffer offer1 = null;
        if (savedPyme != null) {
            // Offer 1: Java + Spring Boot (100% Match for our Freelancer)
            if (jobOfferRepository.findAll().stream().noneMatch(o -> o.getTitle().contains("Java y Spring"))) {
                JobOffer offer = new JobOffer();
                offer.setPyme(savedPyme);
                offer.setTitle("Desarrollador Java y Spring Boot (100% Match)");
                offer.setDescription("Buscamos un desarrollador backend con experiencia en Java y Spring Boot.");
                offer.setBudgetType(BudgetType.FIJO);
                offer.setTotalBudget(1500.0);
                offer.setStatus(OfferStatus.ABIERTA);
                offer.setRequiredSkills(Set.of(java, spring));
                offer1 = jobOfferRepository.save(offer);
            } else {
                offer1 = jobOfferRepository.findAll().stream()
                        .filter(o -> o.getTitle().contains("Java y Spring"))
                        .findFirst().orElse(null);
            }

            // Offer 2: Java + React (React is missing for our Freelancer - Skill Gap)
            if (jobOfferRepository.findAll().stream().noneMatch(o -> o.getTitle().contains("Java y React"))) {
                JobOffer offer = new JobOffer();
                offer.setPyme(savedPyme);
                offer.setTitle("Desarrollador Fullstack Java y React (Skill Gap)");
                offer.setDescription("Buscamos desarrollador para migrar portal heredado a React con backend Java.");
                offer.setBudgetType(BudgetType.POR_HORA);
                offer.setTotalBudget(2500.0);
                offer.setStatus(OfferStatus.ABIERTA);
                offer.setRequiredSkills(Set.of(java, react));
                jobOfferRepository.save(offer);
            }

        }

        // 6. Seed Application, Contract and Milestone for Dispute resolution testing
        if (savedFreelancer != null && offer1 != null) {
            Application application = null;
            if (applicationRepository.findAll().isEmpty()) {
                application = new Application();
                application.setOffer(offer1);
                application.setFreelancer(savedFreelancer);
                application.setProposedAmount(1500.0);
                application.setEstimatedDays(15);
                application.setStatus(ApplicationStatus.ACCEPTED);
                application = applicationRepository.save(application);
            } else {
                application = applicationRepository.findAll().stream().findFirst().orElse(null);
            }

            if (application != null && contractRepository.findAll().isEmpty()) {
                Contract contract = new Contract();
                contract.setApplication(application);
                contract.setDigitalSignatureFreelancer("Signature_Freelancer_Doe");
                contract.setDigitalSignaturePyme("Signature_Pyme_Acme");
                contract.setStatus(ContractStatus.SIGNED);
                Contract savedContract = contractRepository.save(contract);

                Milestone milestone = new Milestone();
                milestone.setContract(savedContract);
                milestone.setTitle("Desarrollo de APIs e Integración de Seguridad");
                milestone.setAmount(750.0);
                milestone.setDeadline(LocalDate.now().plusDays(10));
                milestone.setStatus(MilestoneStatus.PENDING_FUNDING);
                milestoneRepository.save(milestone);
            }
        }
    }

    private Skill getOrSeedSkill(String name, String category) {
        return skillRepository.findByName(name)
                .orElseGet(() -> {
                    Skill skill = new Skill();
                    skill.setName(name);
                    skill.setCategory(category);
                    return skillRepository.save(skill);
                });
    }

    private void seedAssessment(FreelancerProfile freelancer, Skill skill, double score, boolean passed) {
        Assessment assessment = new Assessment();
        assessment.setFreelancer(freelancer);
        assessment.setSkill(skill);
        assessment.setScore(score);
        assessment.setPassed(passed);
        assessment.setQuizData(new HashMap<>());
        assessmentRepository.save(assessment);
    }
}
