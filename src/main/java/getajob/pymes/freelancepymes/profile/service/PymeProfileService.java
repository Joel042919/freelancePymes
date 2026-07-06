package getajob.pymes.freelancepymes.profile.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.repository.PymeProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PymeProfileService {

    private final PymeProfileRepository pymeProfileRepository;

    public PymeProfile getPymeProfile(UUID userId) {
        return pymeProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de PYME no encontrado"));
    }

    public PymeProfile updatePymeProfile(UUID userId, String companyName, String industry) {
        PymeProfile profile = pymeProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de PYME no encontrado"));

        if (companyName != null && !companyName.trim().isEmpty()) {
            profile.setCompanyName(companyName);
        }
        if (industry != null && !industry.trim().isEmpty()) {
            profile.setIndustry(industry);
        }

        return pymeProfileRepository.save(profile);
    }
}
