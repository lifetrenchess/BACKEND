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
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable int id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping
    public ReviewDTO createReview(@RequestBody @Valid CreateReviewDTO dto) {
        return reviewService.createReview(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable int id, @RequestBody @Valid ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    @PostMapping("/{id}/response")
    public ResponseEntity<ReviewDTO> respondToReview(@PathVariable int id, @RequestBody @Valid AgentResponseDTO responseDTO) {
        return ResponseEntity.ok(reviewService.respondToReview(id, responseDTO.getAgentResponse()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        boolean deleted = reviewService.deleteReview(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<ReviewSummaryDTO> getAllReviews() {
        return reviewService.getAllReviewSummaries();
    }
}

