package getajob.pymes.freelancepymes.contract.controller;

import getajob.pymes.freelancepymes.contract.dto.DeliverRequest;
import getajob.pymes.freelancepymes.contract.service.MilestoneLifecycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/marketplace/milestones")
@RequiredArgsConstructor
public class MilestoneLifecycleController {

    private final MilestoneLifecycleService milestoneLifecycleService;

    @PostMapping("/{id}/fund")
    @PreAuthorize("hasRole('PYME')")
    public ResponseEntity<Map<String, String>> fundMilestone(
            @PathVariable UUID id,
            @RequestParam String orderId,
            Principal principal
    ) {
        milestoneLifecycleService.fundMilestone(id, principal.getName(), orderId);
        return ResponseEntity.ok(Map.of("message", "Hito fondeado correctamente (FUNDED)."));
    }

    @PostMapping("/{id}/deliver")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Map<String, String>> deliverMilestone(
            @PathVariable UUID id,
            @Valid @RequestBody DeliverRequest request,
            Principal principal
    ) {
        milestoneLifecycleService.deliverMilestone(id, request, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Entrega registrada correctamente (DELIVERED)."));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('PYME')")
    public ResponseEntity<Map<String, String>> rejectMilestone(
            @PathVariable UUID id,
            Principal principal
    ) {
        milestoneLifecycleService.rejectMilestone(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "Entrega rechazada correctamente (REJECTED)."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
