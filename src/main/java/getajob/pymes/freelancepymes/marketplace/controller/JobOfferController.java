package getajob.pymes.freelancepymes.marketplace.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.marketplace.dto.JobOfferRequestDTO;
import getajob.pymes.freelancepymes.marketplace.dto.JobOfferResponseDTO;
import getajob.pymes.freelancepymes.marketplace.service.JobOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@Tag(name = "Ofertas de Trabajo (Job Offers)")
public class JobOfferController {

    private final JobOfferService jobOfferService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Obtener todas las ofertas publicadas")
    public ResponseEntity<List<JobOfferResponseDTO>> getAllOffers() {
        return ResponseEntity.ok(jobOfferService.getAllOffers());
    }

    @GetMapping("/{offerId}")
    @Operation(summary = "Obtener una oferta por ID")
    public ResponseEntity<JobOfferResponseDTO> getOfferById(@PathVariable UUID offerId) {
        return ResponseEntity.ok(jobOfferService.getOfferById(offerId));
    }

    @GetMapping("/pyme")
    @PreAuthorize("hasRole('PYME')")
    @Operation(summary = "Obtener ofertas de la PYME autenticada")
    public ResponseEntity<List<JobOfferResponseDTO>> getMyOffers(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(jobOfferService.getOffersByPyme(user.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('PYME')")
    @Operation(summary = "Crear una nueva oferta de trabajo (solo PYME)")
    public ResponseEntity<JobOfferResponseDTO> createOffer(
            @Valid @RequestBody JobOfferRequestDTO dto,
            Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        JobOfferResponseDTO response = jobOfferService.createOffer(dto, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{offerId}")
    @PreAuthorize("hasRole('PYME')")
    @Operation(summary = "Actualizar una oferta existente (solo PYME dueña)")
    public ResponseEntity<JobOfferResponseDTO> updateOffer(
            @PathVariable UUID offerId,
            @Valid @RequestBody JobOfferRequestDTO dto,
            Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(jobOfferService.updateOffer(offerId, dto, user.getId()));
    }

    @DeleteMapping("/{offerId}")
    @PreAuthorize("hasRole('PYME')")
    @Operation(summary = "Eliminar una oferta (solo PYME dueña)")
    public ResponseEntity<Void> deleteOffer(
            @PathVariable UUID offerId,
            Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        jobOfferService.deleteOffer(offerId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errors.put(field, msg);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
