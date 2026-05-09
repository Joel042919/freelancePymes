package getajob.pymes.freelancepymes.profile.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import getajob.pymes.freelancepymes.profile.entity.Portfolio;
import getajob.pymes.freelancepymes.profile.service.PortafolioService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/portafolios")
@RequiredArgsConstructor
public class PortafolioController {
    private final PortafolioService portafolioService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearPortafolio(
        @RequestParam("freelancerId") UUID freelancerId,
        @RequestParam("title") String title, 
        @RequestParam("description") String description, 
        @RequestParam(value = "projectUrl",required = false) String projectUrl, 
        @RequestParam("tecnologiasUsadas") List<String> tecnologiasUsadas, 
        @RequestParam("imagenes") List<MultipartFile> imagenes
    ) {
        Portfolio nuevoPortafolio = portafolioService.guardarPortafolio(freelancerId,title, description, projectUrl, tecnologiasUsadas, imagenes);
        return ResponseEntity.ok(nuevoPortafolio);
    }
    
}
