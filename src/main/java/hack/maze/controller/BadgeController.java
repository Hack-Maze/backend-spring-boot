package hack.maze.controller;

import hack.maze.dto.BadgeDTO;
import hack.maze.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/badge")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping
    public ResponseEntity<?> getAllBadges() {
        try {
            return ResponseEntity.ok(badgeService.getAllBadges());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBadge(@ModelAttribute BadgeDTO badgeDTO) {
        try {
            return ResponseEntity.ok(badgeService.createBadge(badgeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{badgeId}")
    public ResponseEntity<?> updateBadge(@PathVariable long badgeId, @ModelAttribute BadgeDTO badgeDTO) {
        try {
            return ResponseEntity.ok(badgeService.updateBadge(badgeId, badgeDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{badgeId}")
    public ResponseEntity<?> deleteBadge(@PathVariable long badgeId) {
        try {
            return ResponseEntity.ok(badgeService.deleteBadge(badgeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
