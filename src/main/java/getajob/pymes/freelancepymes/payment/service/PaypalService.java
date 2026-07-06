package getajob.pymes.freelancepymes.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.Map;

@Service
public class PaypalService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    private final String BASE_URL_SANDBOX = "https://api-m.sandbox.paypal.com";

    private String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String auth = clientId + ":" + clientSecret;
        headers.setBasicAuth(Base64.getEncoder().encodeToString(auth.getBytes()));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL_SANDBOX + "/v1/oauth2/token", request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("Error obtaining PayPal access token");
    }

    public boolean captureOrder(String orderId) {
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>("", headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(BASE_URL_SANDBOX + "/v2/checkout/orders/" + orderId + "/capture", request, Map.class);
            return response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.err.println("Error capturing PayPal order: " + e.getMessage());
            return false;
        }
    }
}
