package cts.tpm.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SightseeingDto {

	private Long sightseeingId;
	@NotBlank(message = "places are required placeName cannot be blank")
	private String placeName;
	@NotBlank(message = "Description for sightseeing is required")
	private String description;
	@NotBlank(message = "Time is required")
	private String time;
}
