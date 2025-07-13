package cts.rcss.service;

import cts.rcss.entity.Review;
import cts.rcss.model.*;
import cts.rcss.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cts.rcss.exception.ResourceNotFoundException;
import cts.rcss.PackageServiceClient;
import cts.rcss.UserServiceClient;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
	
	@Autowired
	private UserServiceClient userServiceClient;

	@Autowired
	private PackageServiceClient packageServiceClient;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewSummaryDTO> getAllReviewSummaries() {
        return reviewRepository.findAll().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(int id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        return convertToDTO(review);
    }

    public ReviewDTO createReview(CreateReviewDTO dto) {
        // Validate user
        try {
            UserDTO user = userServiceClient.getUserById(dto.getuserId());
            if (user == null) {
                throw new ResourceNotFoundException("User not found with ID: " + dto.getuserId());
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to validate user: " + e.getMessage());
        }

        // Validate package
        try {
            PackageDTO pkg = packageServiceClient.getPackageById(dto.getpackageId());
            if (pkg == null) {
                throw new ResourceNotFoundException("Package not found with ID: " + dto.getpackageId());
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to validate package: " + e.getMessage());
        }

        // Create and save review
        Review review = new Review();
        review.setuserId(dto.getuserId());
        review.setpackageId(dto.getpackageId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        // review.setTimestamp(LocalDateTime.now()); // Set current timestamp

        Review saved = reviewRepository.save(review);
        return convertToDTO(saved);
    }

    public ReviewDTO updateReview(int id, ReviewDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));

        // Validate user
        try {
            UserDTO user = userServiceClient.getUserById(dto.getuserId());
            if (user == null) {
                throw new ResourceNotFoundException("User not found with ID: " + dto.getuserId());
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to validate user: " + e.getMessage());
        }

        // Validate package
        try {
            PackageDTO pkg = packageServiceClient.getPackageById(dto.getpackageId());
            if (pkg == null) {
                throw new ResourceNotFoundException("Package not found with ID: " + dto.getpackageId());
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to validate package: " + e.getMessage());
        }

        review.setuserId(dto.getuserId());
        review.setpackageId(dto.getpackageId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setTimestamp(dto.getTimestamp());
        review.setAgentResponse(dto.getAgentResponse());

        return convertToDTO(reviewRepository.save(review));
    }

    public ReviewDTO respondToReview(int id, String response) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with ID: " + id));
        review.setAgentResponse(response);
        return convertToDTO(reviewRepository.save(review));
    }

    public boolean deleteReview(int id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewID(review.getReviewID());
        dto.setuserId(review.getuserId());
        dto.setpackageId(review.getpackageId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setTimestamp(review.getTimestamp());
        dto.setAgentResponse(review.getAgentResponse());
        return dto;
    }

    private ReviewSummaryDTO convertToSummaryDTO(Review review) {
        ReviewSummaryDTO dto = new ReviewSummaryDTO();
        dto.setReviewID(review.getReviewID());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        return dto;
    }
}

