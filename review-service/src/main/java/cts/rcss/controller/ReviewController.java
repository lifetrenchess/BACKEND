package cts.rcss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cts.rcss.model.*;
import cts.rcss.service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable int id) {
        try {
            ReviewDTO review = reviewService.getReviewById(id);
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody @Valid CreateReviewDTO dto) {
        try {
            ReviewDTO created = reviewService.createReview(dto);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create review: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable int id, @RequestBody @Valid ReviewDTO dto) {
        try {
            ReviewDTO updated = reviewService.updateReview(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update review: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/response")
    public ResponseEntity<?> respondToReview(@PathVariable int id, @RequestBody @Valid AgentResponseDTO responseDTO) {
        try {
            ReviewDTO updated = reviewService.respondToReview(id, responseDTO.getAgentResponse());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to respond to review: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable int id) {
        try {
            boolean deleted = reviewService.deleteReview(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete review: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        try {
            List<ReviewSummaryDTO> reviews = reviewService.getAllReviewSummaries();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch reviews: " + e.getMessage());
        }
    }
}

