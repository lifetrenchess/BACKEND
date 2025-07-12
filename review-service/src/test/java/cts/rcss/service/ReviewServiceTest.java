package cts.rcss.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cts.rcss.entity.Review;
import cts.rcss.exception.ResourceNotFoundException;
import cts.rcss.model.CreateReviewDTO;
import cts.rcss.model.ReviewDTO;
import cts.rcss.repository.ReviewRepository;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

@Mock
private ReviewRepository reviewRepository;

@InjectMocks
private ReviewService reviewService;


    private Review review;
    private ReviewDTO reviewDTO;
    private CreateReviewDTO createReviewDTO;
    private LocalDateTime timestamp;

    @BeforeEach
    void setUp() {
        timestamp = LocalDateTime.of(2021, 7, 1, 10, 0);

        review = new Review();
        review.setReviewID(1);
        review.setuserId(101);
        review.setpackageId(201);
        review.setRating(5);
        review.setComment("Excellent trip!");
        review.setTimestamp(timestamp);
        review.setAgentResponse("Thank you!");

        reviewDTO = new ReviewDTO(1, 101, 201, 5, "Excellent trip!", timestamp, "Thank you!");

        createReviewDTO = new CreateReviewDTO();
        createReviewDTO.setuserId(101);
        createReviewDTO.setpackageId(201);
        createReviewDTO.setRating(5);
        createReviewDTO.setComment("Excellent trip!");
        // Note: CreateReviewDTO doesn't have timestamp in your version
    }

    @Test
    void testGetAllReviewSummaries() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        var result = reviewService.getAllReviewSummaries();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getReviewID());
        assertEquals("Excellent trip!", result.get(0).getComment());
    }

    @Test
    void testGetReviewById() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));

        ReviewDTO result = reviewService.getReviewById(1);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Thank you!", result.getAgentResponse());
        assertEquals(timestamp, result.getTimestamp());
    }

    @Test
    void testGetReviewByIdThrowsException() {
        when(reviewRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewById(1));
    }

//    @Test
//    void testCreateReview() {
//        when(reviewRepository.save(any())).thenReturn(review);
//
////        ReviewDTO result = reviewService.createReview(createReviewDTO);
////
////        assertNotNull(result);
////        assertEquals("Excellent trip!", result.getComment());
////        assertEquals(5, result.getRating());
////    }
//
//    @Test
//    void testUpdateReview() {
//        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
//        when(reviewRepository.save(any())).thenReturn(review);
//
////        ReviewDTO result = reviewService.updateReview(1, reviewDTO);
//
////        assertNotNull(result);
////        assertEquals("Excellent trip!", result.getComment());
////        assertEquals("Thank you!", result.getAgentResponse());
////        assertEquals(timestamp, result.getTimestamp());
////    }

    @Test
    void testRespondToReview() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any())).thenReturn(review);

        ReviewDTO result = reviewService.respondToReview(1, "Thanks for your feedback!");

        assertNotNull(result);
        assertEquals("Thanks for your feedback!", result.getAgentResponse());
    }

    @Test
    void testDeleteReviewSuccess() {
        when(reviewRepository.existsById(1)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(1);

        boolean result = reviewService.deleteReview(1);

        assertTrue(result);
    }

    @Test
    void testDeleteReviewFailure() {
        when(reviewRepository.existsById(1)).thenReturn(false);

        boolean result = reviewService.deleteReview(1);

        assertFalse(result);
    }
}
