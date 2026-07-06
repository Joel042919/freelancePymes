package getajob.pymes.freelancepymes.profile.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import getajob.pymes.freelancepymes.profile.entity.Portfolio;
import getajob.pymes.freelancepymes.profile.service.PortafolioService;
import getajob.pymes.freelancepymes.profile.service.S3Service;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/portafolios")
@RequiredArgsConstructor
public class PortafolioController {

    public record PortfolioDTO(UUID id, String title, String description, String projectUrl, List<String> tecnologiasUsadas, List<String> imageUrl) {}

    private PortfolioDTO toDto(Portfolio p) {
        return new PortfolioDTO(
            p.getId(),
            p.getTitle(),
            p.getDescription(),
            p.getProjectUrl(),
            p.getTecnologiasUsadas(),
            p.getImageUrl() == null ? List.of() : p.getImageUrl().stream().map(s3Service::generatePresignedGetUrl).collect(Collectors.toList())
        );
    }
    private final PortafolioService portafolioService;
    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<?> getPresignedUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("contentType") String contentType
    ) {
        try {
            return ResponseEntity.ok(s3Service.generatePresignedUrl(fileName, contentType));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearPortafolio(
        @RequestParam("freelancerId") UUID freelancerId,
        @RequestParam("title") String title, 
        @RequestParam("description") String description, 
        @RequestParam(value = "projectUrl",required = false) String projectUrl, 
        @RequestParam("tecnologiasUsadas") List<String> tecnologiasUsadas, 
        @RequestParam(value = "imagenes", required = false) List<String> imagenes
    ) {
        Portfolio nuevoPortafolio = portafolioService.guardarPortafolio(freelancerId,title, description, projectUrl, tecnologiasUsadas, imagenes);
        return ResponseEntity.ok(toDto(nuevoPortafolio));
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<PortfolioDTO>> getPortafolios(@PathVariable UUID freelancerId) {
        List<PortfolioDTO> result = portafolioService.getPorFreelancer(freelancerId).stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPortafolio(
        @PathVariable UUID id,
        @RequestParam(value = "title", required = false) String title, 
        @RequestParam(value = "description", required = false) String description, 
        @RequestParam(value = "projectUrl", required = false) String projectUrl, 
        @RequestParam(value = "tecnologiasUsadas", required = false) List<String> tecnologiasUsadas, 
        @RequestParam(value = "imagenes", required = false) List<String> imagenes
    ) {
        Portfolio actualizado = portafolioService.actualizarPortafolio(id, title, description, projectUrl, tecnologiasUsadas, imagenes);
        return ResponseEntity.ok(toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPortafolio(@PathVariable UUID id) {
        portafolioService.eliminarPortafolio(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<?> eliminarImagen(
        @PathVariable UUID id,
        @RequestParam("imageUrl") String imageUrl
    ) {
        Portfolio actualizado = portafolioService.eliminarImagen(id, imageUrl);
        return ResponseEntity.ok(toDto(actualizado));
    }
}
