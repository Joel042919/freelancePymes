package getajob.pymes.freelancepymes.profile.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;

import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Portfolio;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.PortafolioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortafolioService {
    private final PortafolioRepository portafolioRepository;
    private final S3Service s3Service;
    private final FreelanceProfileRepository freelanceRepository;

    public Portfolio guardarPortafolio(UUID freelancerId,String title, String description, String projectUrl, List<String> tecnologias, List<String> imagenes){
        FreelancerProfile freelancer = freelanceRepository.findById(freelancerId)
                .orElseThrow(()-> new RuntimeException("Freelancer no encontrado"));

        Portfolio portfolio = new Portfolio();
        portfolio.setFreelancer(freelancer);
        portfolio.setTitle(title);
        portfolio.setDescription(description);
        portfolio.setProjectUrl(projectUrl);
        portfolio.setTecnologiasUsadas(tecnologias);
        portfolio.setImageUrl(imagenes);

        return portafolioRepository.save(portfolio);
    }

    public List<Portfolio> getPorFreelancer(UUID freelancerId) {
        return portafolioRepository.findByFreelancerId(freelancerId);
    }

    public Portfolio actualizarPortafolio(UUID portfolioId, String title, String description, String projectUrl, List<String> tecnologias, List<String> nuevasImagenes) {
        Portfolio portfolio = portafolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portafolio no encontrado"));

        if (title != null) portfolio.setTitle(title);
        if (description != null) portfolio.setDescription(description);
        if (projectUrl != null) portfolio.setProjectUrl(projectUrl);
        if (tecnologias != null) portfolio.setTecnologiasUsadas(tecnologias);

        if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
            List<String> urlsActuales = portfolio.getImageUrl();
            if (urlsActuales == null) {
                portfolio.setImageUrl(nuevasImagenes);
            } else {
                urlsActuales.addAll(nuevasImagenes);
                portfolio.setImageUrl(urlsActuales);
            }
        }

        return portafolioRepository.save(portfolio);
    }

    public void eliminarPortafolio(UUID portfolioId) {
        if (!portafolioRepository.existsById(portfolioId)) {
            throw new RuntimeException("Portafolio no encontrado");
        }
        portafolioRepository.deleteById(portfolioId);
    }

    public Portfolio eliminarImagen(UUID portfolioId, String imageUrl) {
        Portfolio portfolio = portafolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portafolio no encontrado"));

        List<String> urlsActuales = portfolio.getImageUrl();
        if (urlsActuales != null && urlsActuales.contains(imageUrl)) {
            urlsActuales.remove(imageUrl);
            portfolio.setImageUrl(urlsActuales);
        } else {
            throw new RuntimeException("La imagen no existe en este proyecto");
        }

        return portafolioRepository.save(portfolio);
    }
}
