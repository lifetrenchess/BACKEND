
package cts.rcss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cts.rcss.model.*;
import cts.rcss.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private ReviewDTO reviewDTO;
    private CreateReviewDTO createReviewDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        LocalDateTime timestamp = LocalDateTime.of(2021, 7, 1, 10, 0);

        reviewDTO = new ReviewDTO(1, 101, 201, 5, "Great package!", timestamp, "Thank you!");
        createReviewDTO = new CreateReviewDTO();
        createReviewDTO.setuserId(101);
        createReviewDTO.setpackageId(201);
        createReviewDTO.setRating(5);
        createReviewDTO.setComment("Great package!");
    }

    @Test
    void testCreateReview() throws Exception {
//        when(reviewService.createReview(any(CreateReviewDTO.class))).thenReturn(reviewDTO);

        mockMvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Great package!"));
    }

    @Test
    void testGetAllReviews() throws Exception {
        ReviewSummaryDTO summary = new ReviewSummaryDTO();
        summary.setReviewID(1);
        summary.setRating(5);
        summary.setComment("Great package!");

        when(reviewService.getAllReviewSummaries()).thenReturn(Collections.singletonList(summary));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetReviewById() throws Exception {
        when(reviewService.getReviewById(1)).thenReturn(reviewDTO);

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testUpdateReview() throws Exception {
        ReviewDTO updatedDTO = new ReviewDTO(1, 101, 201, 4, "Updated comment", LocalDateTime.of(2021, 7, 1, 10, 0), "Thanks!");

//        when(reviewService.updateReview(eq(1), any(ReviewDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Updated comment"));
    }

    @Test
    void testRespondToReview() throws Exception {
        AgentResponseDTO responseDTO = new AgentResponseDTO();
        responseDTO.setAgentResponse("Thank you for your feedback!");

        reviewDTO.setAgentResponse(responseDTO.getAgentResponse());
        when(reviewService.respondToReview(eq(1), anyString())).thenReturn(reviewDTO);

        mockMvc.perform(post("/api/reviews/1/response")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agentResponse").value("Thank you for your feedback!"));
    }

    @Test
    void testDeleteReview() throws Exception {
        when(reviewService.deleteReview(1)).thenReturn(true);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }
}

