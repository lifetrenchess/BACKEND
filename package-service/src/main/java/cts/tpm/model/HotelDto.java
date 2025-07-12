package cts.tpm.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {

	private Long hotelId;
	@NotBlank(message = "Hotel name is required")
	private String name;
	@NotBlank(message = "Hotel location is required")
	private String location;
	@Min(value = 1, message = "rating must be atleast 1")
	@Max(value = 5, message = "rating must not exceed 5")
	private int starRating;
	@NotBlank(message = "Check-in time is required")
	private String checkInTime;
	@NotBlank(message = "Check-out time is required")
	private String checkOutTime;

}
