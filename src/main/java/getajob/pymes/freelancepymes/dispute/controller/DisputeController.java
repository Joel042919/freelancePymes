package getajob.pymes.freelancepymes.dispute.controller;

import getajob.pymes.freelancepymes.dispute.dto.AppealRequest;
import getajob.pymes.freelancepymes.dispute.dto.DisputeResponse;
import getajob.pymes.freelancepymes.dispute.service.DisputeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/disputes")
@RequiredArgsConstructor
public class DisputeController {

    private final DisputeService disputeService;

    @PostMapping("/milestones/{milestoneId}/appeal")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<DisputeResponse> appealMilestone(
            @PathVariable UUID milestoneId,
            @Valid @RequestBody AppealRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(disputeService.appealMilestone(milestoneId, request, principal.getName()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
