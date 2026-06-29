package getajob.pymes.freelancepymes.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class NotificationService {

    public boolean sendDisputeReport(String recipientEmail, String subject, Map<String, Object> resolutionReport) {
        log.info("========================================= NOTIFICATION SYSTEM =========================================");
        log.info("Sending Email notification to: {}", recipientEmail);
        log.info("Subject: {}", subject);
        log.info("Verdict: {}", resolutionReport.get("verdict"));
        log.info("Reasoning: {}", resolutionReport.get("reasoning"));
        log.info("Compliance Checklist:");
        if (resolutionReport.get("contractCompliance") instanceof java.util.List) {
            java.util.List<?> list = (java.util.List<?>) resolutionReport.get("contractCompliance");
            for (Object obj : list) {
                log.info("  - {}", obj);
            }
        }
        log.info("Agent: {}", resolutionReport.get("agentName"));
        log.info("Date: {}", resolutionReport.get("resolutionDate"));
        log.info("=======================================================================================================");
        return true;
    }
}
