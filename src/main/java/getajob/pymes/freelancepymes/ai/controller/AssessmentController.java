package getajob.pymes.freelancepymes.ai.controller;

import getajob.pymes.freelancepymes.ai.dto.AssessmentResultResponse;
import getajob.pymes.freelancepymes.ai.dto.SkillStatusResponse;
import getajob.pymes.freelancepymes.ai.dto.StartAssessmentResponse;
import getajob.pymes.freelancepymes.ai.dto.SubmitAssessmentRequest;
import getajob.pymes.freelancepymes.ai.service.AssessmentService;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/assessments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('FREELANCER')")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @GetMapping("/my-skills")
    public ResponseEntity<List<SkillStatusResponse>> getMySkills(Principal principal) {
        return ResponseEntity.ok(assessmentService.getMySkills(principal.getName()));
    }

    @GetMapping("/my-badges")
    public ResponseEntity<List<SkillStatusResponse>> getMyBadges(Principal principal) {
        return ResponseEntity.ok(assessmentService.getMyBadges(principal.getName()));
    }

    @PostMapping("/{skillId}/start")
    public ResponseEntity<StartAssessmentResponse> startAssessment(
            @PathVariable Integer skillId,
            Principal principal
    ) {
        return ResponseEntity.ok(assessmentService.startAssessment(skillId, principal.getName()));
    }

    @PostMapping("/{assessmentId}/submit")
    public ResponseEntity<AssessmentResultResponse> submitAssessment(
            @PathVariable UUID assessmentId,
            @Valid @RequestBody SubmitAssessmentRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(assessmentService.submitAssessment(assessmentId, request.getAnswers(), principal.getName()));
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
