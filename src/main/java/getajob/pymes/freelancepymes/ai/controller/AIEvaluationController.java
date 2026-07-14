package getajob.pymes.freelancepymes.ai.controller;

import getajob.pymes.freelancepymes.ai.service.AIEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/v1/evaluation")
@RequiredArgsConstructor
public class AIEvaluationController {

    private final AIEvaluationService aiEvaluationService;

    @PostMapping("/voice")
    public ResponseEntity<byte[]> processVoiceInterview(@RequestParam("audio") MultipartFile audioFile) {
        try {
            // 1. Transcribe the audio
            String transcript = aiEvaluationService.transcribeAudio(audioFile.getBytes());
            
            // 2. Get LLM response based on transcript
            String aiResponseText = aiEvaluationService.getAIResponse(transcript);

            // 3. Convert LLM response to speech
            byte[] aiAudio = aiEvaluationService.textToSpeech(aiResponseText);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("audio/mpeg"));
            headers.set("X-Transcript", transcript); // We can return the transcript in a header
            headers.set("X-AI-Response", aiResponseText);

            return new ResponseEntity<>(aiAudio, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
