package getajob.pymes.freelancepymes.payment.controller;

import getajob.pymes.freelancepymes.payment.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments/paypal")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final PaypalService paypalService;

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> payload) {
        // En producción validaríamos que el milestone exista y obtendríamos su monto.
        Double amount = Double.valueOf(payload.get("amount").toString());
        Map<String, Object> order = paypalService.createOrder(amount);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/capture-order")
    public ResponseEntity<Map<String, Object>> captureOrder(@RequestBody Map<String, Object> payload) {
        String orderId = payload.get("orderId").toString();
        boolean captured = paypalService.captureOrder(orderId);
        
        if(captured) {
            System.out.println("PayPal Order Captured: " + orderId);
            return ResponseEntity.ok(Map.of("status", "COMPLETED"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to capture order"));
        }
    }
}
