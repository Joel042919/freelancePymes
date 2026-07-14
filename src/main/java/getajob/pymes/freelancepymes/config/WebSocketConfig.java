package getajob.pymes.freelancepymes.config;

import getajob.pymes.freelancepymes.ai.controller.AIEvaluationWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final AIEvaluationWebSocketHandler aiEvaluationWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(aiEvaluationWebSocketHandler, "/ws/evaluation")
                .setAllowedOrigins("http://localhost:3000"); // Permite CORS para Next.js
    }
}
