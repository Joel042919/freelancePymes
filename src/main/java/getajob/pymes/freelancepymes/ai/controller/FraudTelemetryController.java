package getajob.pymes.freelancepymes.ai.controller;

import getajob.pymes.freelancepymes.ai.entity.FraudTelemetryLog;
import getajob.pymes.freelancepymes.ai.enums.FraudEventType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/v1/telemetry")
public class FraudTelemetryController {

    @PostMapping("/fraud")
    public ResponseEntity<String> reportFraud(@RequestBody Map<String, Object> payload) {
        // TODO: Save to database using a service and repository
        System.out.println("⚠️ Fraud Event Detected: " + payload);
        return ResponseEntity.ok("Fraud event logged successfully");
    }
}
