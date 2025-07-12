package cts.tpm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hotelId;

	private String name;
	private String location;
	private int starRating;
	private String checkInTime;
	private String checkOutTime;

	@ManyToOne
	@JoinColumn(name = "package_id")
	private TravelPackage travelPackage;

}
