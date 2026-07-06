package getajob.pymes.freelancepymes.profile.controller;

import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.service.FreelancerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/v1/profiles/freelancer")
@RequiredArgsConstructor
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    @GetMapping("/{id}")
    public ResponseEntity<FreelancerProfile> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FreelancerProfile> updateProfile(
            @PathVariable UUID id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) List<Integer> skillIds
    ) {
        return ResponseEntity.ok(profileService.updateProfile(id, firstName, lastName, bio, skillIds));
    }
}
