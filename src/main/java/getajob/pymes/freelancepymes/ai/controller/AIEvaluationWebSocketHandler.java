package getajob.pymes.freelancepymes.ai.controller;

import getajob.pymes.freelancepymes.ai.service.AIEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AIEvaluationWebSocketHandler extends AbstractWebSocketHandler {

    private final AIEvaluationService aiService;
    private final Map<String, Integer> sessionQuestionsCount = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection: " + session.getId());
        sessionQuestionsCount.put(session.getId(), 0);
        
        // La IA inicia la conversación
        String welcomeText = "Hola, bienvenido a tu evaluación. Empecemos. Háblame de tu experiencia con React.";
        byte[] audio = aiService.textToSpeech(welcomeText);
        
        // Enviamos un JSON indicando que viene audio y el texto de la transcripción
        String metadata = String.format("{\"type\":\"ai_response\", \"text\":\"%s\"}", welcomeText);
        session.sendMessage(new TextMessage(metadata));
        
        // Enviamos el buffer de audio
        if (audio.length > 0) {
            session.sendMessage(new BinaryMessage(audio));
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        byte[] payload = message.getPayload().array();
        
        // 1. Transcribir el audio del usuario usando Deepgram
        String transcript = aiService.transcribeAudio(payload);
        
        // Informamos al cliente qué fue lo que transcribimos
        session.sendMessage(new TextMessage(String.format("{\"type\":\"user_transcript\", \"text\":\"%s\"}", transcript)));

        int questionCount = sessionQuestionsCount.getOrDefault(session.getId(), 0) + 1;
        sessionQuestionsCount.put(session.getId(), questionCount);

        String aiResponseText;
        if (questionCount >= 10) {
            aiResponseText = "Excelente, hemos concluido la evaluación teórica. Gracias por tus respuestas.";
        } else {
            // 2. Pasamos el transcript a OpenAI (GPT-4o) para obtener la siguiente pregunta
            aiResponseText = aiService.getAIResponse(transcript);
        }

        // 3. Pasamos el texto generado a Deepgram TTS para el audio de respuesta
        byte[] aiAudio = aiService.textToSpeech(aiResponseText);
        
        // Enviamos metadata
        session.sendMessage(new TextMessage(String.format("{\"type\":\"ai_response\", \"text\":\"%s\"}", aiResponseText)));
        
        // Enviamos audio
        if (aiAudio.length > 0) {
            session.sendMessage(new BinaryMessage(aiAudio));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessionQuestionsCount.remove(session.getId());
        System.out.println("WebSocket connection closed: " + session.getId());
    }
}
