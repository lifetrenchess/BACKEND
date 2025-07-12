package cts.tpm.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {

	private Long flightId;
	@NotBlank(message = "Airline name cannot be blank")
	private String airline;
	@NotBlank(message = "Must specify the departure location ")
	private String departure;
	@NotBlank(message = "Must specify the Arrival location")
	private String arrival;
	@NotBlank(message = "Departure time is required")
	private String departureTime;
	@NotBlank(message = "Arrival time is required")
	private String arrivalTime;

}
