package getajob.pymes.freelancepymes.profile.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.service.PymeProfileService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pyme/profile")
@RequiredArgsConstructor
public class PymeProfileController {

    private final PymeProfileService pymeProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<PymeProfile> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(pymeProfileService.getPymeProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<PymeProfile> updateProfile(
            @PathVariable UUID userId,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String industry) {
        return ResponseEntity.ok(pymeProfileService.updatePymeProfile(userId, companyName, industry));
    }
}
