package cts.tpm.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelPackageDto {

	private Long packageId;
	
	@NotBlank(message = "Title cannot be blank")
	@Size(max = 150, message = "Title must be within 150 characters")
	private String title;
	
	@NotBlank(message = "Description cannot be blank")
	@Size(max = 500, message = "Description must be within 500 characters")
	private String description;
	
	@Positive(message = "Duration must be a positive number")
	private Integer duration;
	
	@Positive(message = "Price must be a positive value")
	private double price;
	
	@NotBlank(message = "Destination cannot be blank")
	private String destination;
	
	@NotBlank(message = "Included services cannot be blank")
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

	private List<FlightDto> flights;
	private List<HotelDto> hotels;
	private List<SightseeingDto> sightseeingList;
}
