package getajob.pymes.freelancepymes.profile.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Portfolio guardarPortafolio(UUID freelancerId,String title, String description, String projectUrl, List<String> tecnologias, List<MultipartFile> imagenes){
        FreelancerProfile freelancer = freelanceRepository.findById(freelancerId)
                .orElseThrow(()-> new RuntimeException("Freelancer no encontrado"));
                
        List<String> imageUrls = s3Service.uploadImages(imagenes);

        Portfolio portfolio = new Portfolio();
        portfolio.setFreelancer(freelancer);
        portfolio.setTitle(title);
        portfolio.setDescription(description);
        portfolio.setProjectUrl(projectUrl);
        portfolio.setTecnologiasUsadas(tecnologias);
        portfolio.setImageUrl(imageUrls);

        return portafolioRepository.save(portfolio);
    }
}
