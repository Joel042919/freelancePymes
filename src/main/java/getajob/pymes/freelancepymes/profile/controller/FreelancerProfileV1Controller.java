package getajob.pymes.freelancepymes.profile.controller;

import java.util.List;
import java.util.Map;

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
@RequestMapping("/api/v1/freelancers/profile")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profile V1", description = "Gestión de perfil de freelancer con endpoints /me")
public class FreelancerProfileV1Controller {

    private final FreelancerProfileService profileService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Obtener perfil del usuario autenticado", description = "Retorna el perfil del freelancer autenticado. Si no existe, lo crea automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado/creado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> getMyProfile(
            @AuthenticationPrincipal User authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        FreelancerProfileResponseDTO profile = profileService.getOrCreateProfile(authenticatedUser.getId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Actualizar perfil del usuario autenticado", description = "Actualiza la información del perfil (nombre, bio, skills)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> updateMyProfile(
            @RequestBody FreelancerProfileRequestDTO dto,
            @AuthenticationPrincipal User authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        FreelancerProfileResponseDTO updatedProfile = profileService.updateProfile(authenticatedUser.getId(), dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/me/skills")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Agregar skills al perfil del usuario autenticado", description = "Agrega una o varias habilidades al perfil sin reemplazar las existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skills agregados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Lista de skills vacía o inválida"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> addSkillsToMyProfile(
            @RequestBody Map<String, List<String>> body,
            @AuthenticationPrincipal User authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        List<String> skillNames = body.get("skillNames");
        if (skillNames == null || skillNames.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista de skills no puede estar vacía");
        }
        FreelancerProfileResponseDTO updatedProfile = profileService.addSkills(authenticatedUser.getId(), skillNames);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/me/skills/{skillId}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    @Operation(summary = "Eliminar skill del perfil del usuario autenticado", description = "Remueve una habilidad específica del perfil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill eliminado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Skill no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> removeSkillFromMyProfile(
            @Parameter(description = "ID del skill a eliminar", required = true) @PathVariable Integer skillId,
            @AuthenticationPrincipal User authenticatedUser) {
        if (authenticatedUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        FreelancerProfileResponseDTO updatedProfile = profileService.removeSkill(authenticatedUser.getId(), skillId);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener perfil público de un freelancer", description = "Retorna el perfil público de un freelancer por su ID de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<FreelancerProfileResponseDTO> getPublicProfile(
            @Parameter(description = "ID del usuario freelancer", required = true) @PathVariable java.util.UUID userId) {
        FreelancerProfileResponseDTO profile = profileService.getOrCreateProfile(userId);
        return ResponseEntity.ok(profile);
    }
}
