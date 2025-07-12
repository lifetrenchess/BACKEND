package cts.rcss.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
//import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "review_info")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewID;

    private long userId;
    private long packageId;
    private int rating;
    private String comment;
    private LocalDateTime timestamp;
    private String agentResponse;

    public int getReviewID() { return reviewID; }
    public void setReviewID(int reviewID) { this.reviewID = reviewID; }

    public long getuserId() { return userId; }
    public void setuserId(long userId) { this.userId = userId; }

    public long getpackageId() { return packageId; }
    public void setpackageId(long packageId) { this.packageId = packageId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

   // public int getTimestamp() { return timestamp; }
    //public void setTimestamp(int timestamp) { this.timestamp = timestamp; }

    public String getAgentResponse() { return agentResponse; }
    public void setAgentResponse(String agentResponse) { this.agentResponse = agentResponse; }
    

@PrePersist
protected void onCreate() {
this.timestamp = LocalDateTime.now();
}

public LocalDateTime getTimestamp() {
return timestamp;
}

public void setTimestamp(LocalDateTime timestamp) {
this.timestamp = timestamp;
}


}