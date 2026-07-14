package getajob.pymes.freelancepymes.ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class AIEvaluationService {

    @Value("${app.ai.openai-key}")
    private String openAiKey;

    @Value("${app.ai.deepgram-key}")
    private String deepgramKey;

    private final RestTemplate restTemplate;

    public AIEvaluationService() {
        this.restTemplate = new RestTemplate();
    }

    public String transcribeAudio(byte[] audioData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + deepgramKey);
        headers.setContentType(MediaType.valueOf("audio/webm"));

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioData, headers);
        String url = "https://api.deepgram.com/v1/listen?language=es&model=nova-2";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> results = (Map<String, Object>) response.getBody().get("results");
                if (results != null) {
                    Map<String, Object> channels = (Map<String, Object>) ((java.util.List<?>) results.get("channels")).get(0);
                    Map<String, Object> alternatives = (Map<String, Object>) ((java.util.List<?>) channels.get("alternatives")).get(0);
                    return (String) alternatives.get("transcript");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No se pudo transcribir el audio.";
    }

    public String getAIResponse(String transcript) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "Eres un entrevistador técnico para evaluar a un freelancer. El candidato dijo: \"" + transcript + "\". Responde de manera concisa y profesional con la siguiente pregunta o comentario.";

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", java.util.List.of(
                        Map.of("role", "system", "content", "Eres un entrevistador técnico evaluando habilidades."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 150
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = "https://api.openai.com/v1/chat/completions";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> choices = (Map<String, Object>) ((java.util.List<?>) response.getBody().get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) choices.get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Lo siento, hubo un error procesando tu respuesta.";
    }

    public byte[] textToSpeech(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + deepgramKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("text", text);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        // Use the requested model: aura-2-celeste-es
        String actualUrl = "https://api.deepgram.com/v1/speak?model=aura-2-celeste-es";

        try {
            ResponseEntity<byte[]> response = restTemplate.postForEntity(actualUrl, requestEntity, byte[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String generateInteractiveContent(String topic, String description) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = "Genera un módulo de aprendizaje interactivo en formato JSON sobre el tema: '" + topic + "' (" + description + "). " +
                "El JSON debe tener estrictamente este formato: " +
                "{\"teoria\": \"Explicación concisa y amigable del tema en 2 párrafos\", " +
                "\"quiz\": [{\"pregunta\": \"Pregunta 1\", \"opciones\": [\"A\", \"B\", \"C\"], \"respuesta_correcta\": 0}]} (Devuelve solo el JSON, sin backticks ni markdown).";

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", java.util.List.of(
                        Map.of("role", "system", "content", "Eres un tutor técnico experto."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 500,
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = "https://api.openai.com/v1/chat/completions";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> choices = (Map<String, Object>) ((java.util.List<?>) response.getBody().get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) choices.get("message");
                String content = (String) message.get("content");
                return content.replace("```json", "").replace("```", "").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Fallback mock JSON si la API falla o no hay token
        return "{\n" +
               "  \"teoria\": \"El tema " + topic + " es fundamental en el desarrollo de software moderno. " + description + ".\\n\\nDominar esto te permitirá crear aplicaciones más robustas y eficientes.\",\n" +
               "  \"quiz\": [\n" +
               "    {\n" +
               "      \"pregunta\": \"¿Cuál es el propósito principal de " + topic + "?\",\n" +
               "      \"opciones\": [\"Optimizar el rendimiento\", \"Complicar el código\", \"Ninguno\"],\n" +
               "      \"respuesta_correcta\": 0\n" +
               "    }\n" +
               "  ]\n" +
               "}";
    }
}
