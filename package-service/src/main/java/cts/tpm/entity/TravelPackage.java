package cts.tpm.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelPackage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long packageId;
	
	private String title;
	private String description;
	private int duration;
	private double price;
	private String destination;
	private String includeService;
	private String excludeService;
	private String highlights;
	private String mainImage; // Primary image for display
	private String images; // JSON array of additional images
	private boolean active = true; // Default to active
	
	// Agent management fields
	private Long createdByAgentId; // ID of the agent who created this package
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Flight> flights;

	@OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Hotel> hotels;

	@OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Sightseeing> sightseeingList;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
