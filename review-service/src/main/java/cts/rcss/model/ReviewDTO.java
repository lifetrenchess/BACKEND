package cts.rcss.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ReviewDTO {

    private int reviewID;

    @Positive(message = "User ID must be positive")
    private long userId;

    @Positive(message = "Package ID must be positive")
    private long packageId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Comment cannot be blank")
    @Size(max = 500, message = "Comment must be less than 500 characters")
    private String comment;
//
//    @Positive(message = "Timestamp must be a positive integer")
//    private int timestamp;

    private LocalDateTime timestamp;

    @Size(max = 500, message = "Agent response must be less than 500 characters")
    private String agentResponse;

    public ReviewDTO() {}

    public ReviewDTO(int reviewID, long userId, long packageId, int rating, String comment, LocalDateTime timestamp) {
        this.reviewID = reviewID;
        this.userId = userId;
        this.packageId = packageId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public ReviewDTO(int reviewID, long userId,long packageId, int rating, String comment, LocalDateTime timestamp, String agentResponse) {
        this.reviewID = reviewID;
        this.userId = userId;
        this.packageId = packageId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.agentResponse = agentResponse;
    }

	public int getReviewID() {
		return reviewID;
	}

	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}

	public long getuserId() {
		return userId;
	}

	public void setuserId(long userId) {
		this.userId = userId;
	}

	public long getpackageId() {
		return packageId;
	}

	public void setpackageId(long packageId) {
		this.packageId = packageId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getAgentResponse() {
		return agentResponse;
	}

	public void setAgentResponse(String agentResponse) {
		this.agentResponse = agentResponse;
	}

    // Getters and setters...
}

