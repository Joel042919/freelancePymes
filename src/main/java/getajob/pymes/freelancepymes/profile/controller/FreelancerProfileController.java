package getajob.pymes.freelancepymes.profile.controller;

import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.service.FreelancerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/v1/profiles/freelancer")
@RequiredArgsConstructor
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileRequestDTO;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileResponseDTO;
import getajob.pymes.freelancepymes.profile.dto.SkillDTO;
import getajob.pymes.freelancepymes.profile.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/freelancers/perfil")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profiles", description = "Gestión de perfiles de freelancers")
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<FreelancerProfile> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FreelancerProfile> updateProfile(
            @PathVariable UUID id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) List<Integer> skillIds
    ) {
        return ResponseEntity.ok(profileService.updateProfile(id, firstName, lastName, bio, skillIds));
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Obtener perfil de freelancer", description = "Retorna el perfil del freelancer por su ID de usuario. Si no existe, lo crea automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado/creado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver este perfil"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> getProfile(
            @Parameter(description = "ID del usuario freelancer", required = true) @PathVariable UUID userId,
            @AuthenticationPrincipal User authenticatedUser) {
        verifyOwnershipOrAdmin(userId, authenticatedUser);
        FreelancerProfileResponseDTO profile = profileService.getOrCreateProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Actualizar perfil de freelancer", description = "Actualiza la información del perfil (nombre, bio, skills)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para editar este perfil"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> updateProfile(
            @Parameter(description = "ID del usuario freelancer", required = true) @PathVariable UUID userId,
            @RequestBody FreelancerProfileRequestDTO dto,
            @AuthenticationPrincipal User authenticatedUser) {
        verifyOwnershipOrAdmin(userId, authenticatedUser);
        FreelancerProfileResponseDTO updatedProfile = profileService.updateProfile(userId, dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{userId}/skills")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Agregar skills al perfil", description = "Agrega una o varias habilidades al perfil del freelancer sin reemplazar las existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skills agregados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Lista de skills vacía o inválida"),
            @ApiResponse(responseCode = "403", description = "No autorizado para editar este perfil"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })

    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> addSkills(
            @Parameter(description = "ID del usuario freelancer", required = true) @PathVariable UUID userId,
            @RequestBody Map<String, List<String>> body,
            @AuthenticationPrincipal User authenticatedUser) {
        verifyOwnershipOrAdmin(userId, authenticatedUser);
        List<String> skillNames = body.get("skillNames");
        if (skillNames == null || skillNames.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista de skills no puede estar vacía");
        }
        FreelancerProfileResponseDTO updatedProfile = profileService.addSkills(userId, skillNames);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{userId}/skills/{skillId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Eliminar skill del perfil", description = "Remueve una habilidad específica del perfil del freelancer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para editar este perfil"),
            @ApiResponse(responseCode = "404", description = "Skill o perfil no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> removeSkill(
            @Parameter(description = "ID del usuario freelancer", required = true) @PathVariable UUID userId,
            @Parameter(description = "ID del skill a eliminar", required = true) @PathVariable Integer skillId,
            @AuthenticationPrincipal User authenticatedUser) {
        verifyOwnershipOrAdmin(userId, authenticatedUser);
        FreelancerProfileResponseDTO updatedProfile = profileService.removeSkill(userId, skillId);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/skills/disponibles")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Listar todas las skills disponibles", description = "Retorna todas las skills del sistema para usar en formularios")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        List<SkillDTO> skills = profileService.getAllAvailableSkills();
        return ResponseEntity.ok(skills);
    }

    private void verifyOwnershipOrAdmin(UUID userId, User authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        boolean isAdmin = authenticatedUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !userId.equals(authenticatedUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para acceder a este perfil");
        }
    }
}
