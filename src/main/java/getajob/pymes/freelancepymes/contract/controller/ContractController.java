package getajob.pymes.freelancepymes.contract.controller;

import getajob.pymes.freelancepymes.contract.dto.ContractResponse;
import getajob.pymes.freelancepymes.contract.dto.SignContractRequest;
import getajob.pymes.freelancepymes.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @GetMapping("/my")
    public ResponseEntity<List<ContractResponse>> getMyContracts(Principal principal) {
        return ResponseEntity.ok(contractService.getMyContracts(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponse> getContract(@PathVariable UUID id, Principal principal) {
        return ResponseEntity.ok(contractService.getContract(id, principal.getName()));
    }

    @PatchMapping("/{id}/sign")
    public ResponseEntity<ContractResponse> signContract(
            @PathVariable UUID id,
            @Valid @RequestBody SignContractRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(contractService.signContract(id, request.getSignatureImage(), principal.getName()));
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
