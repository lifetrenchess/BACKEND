package cts.rcss.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateReviewDTO {

    @Positive(message = "User ID must be positive")
    private long userId;

    @Positive(message = "Package ID must be positive")
    private long packageId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPackageId() {
		return packageId;
	}

	public void setPackageId(long packageId) {
		this.packageId = packageId;
	}
	//The method getpackageId() is undefined for the type CreateReviewDTO
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

//	public int getTimestamp() {
//		return timestamp;
//	}

//	public void setTimestamp(int timestamp) {
//		this.timestamp = timestamp;
//	}

	@NotBlank(message = "Comment cannot be blank")
    @Size(max = 500, message = "Comment must be less than 500 characters")
    private String comment;

//    @Positive(message = "Timestamp must be a positive integer")
//    private int timestamp;

    // Getters and setters...
}
