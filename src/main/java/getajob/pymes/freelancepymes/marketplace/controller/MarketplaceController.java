package getajob.pymes.freelancepymes.marketplace.controller;

import getajob.pymes.freelancepymes.marketplace.dto.ApplicationResponse;
import getajob.pymes.freelancepymes.marketplace.dto.ApplyRequest;
import getajob.pymes.freelancepymes.marketplace.dto.SkillGapResponse;
import getajob.pymes.freelancepymes.marketplace.exception.SkillGapException;
import getajob.pymes.freelancepymes.marketplace.service.MarketplaceService;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @GetMapping("/offers/{offerId}/check-gap")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<SkillGapResponse> checkGap(
            @PathVariable UUID offerId,
            Principal principal
    ) {
        return ResponseEntity.ok(marketplaceService.checkGap(offerId, principal.getName()));
    }

    @PostMapping("/offers/{offerId}/apply")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ApplicationResponse> applyToOffer(
            @PathVariable UUID offerId,
            @Valid @RequestBody ApplyRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(marketplaceService.applyToOffer(offerId, request, principal.getName()));
    }

    @ExceptionHandler(SkillGapException.class)
    public ResponseEntity<Map<String, Object>> handleSkillGapException(SkillGapException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("missingSkills", ex.getMissingSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        body.put("studyPlans", ex.getStudyPlans());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }
}
