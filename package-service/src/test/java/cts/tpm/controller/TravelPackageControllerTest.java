package cts.tpm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import cts.tpm.model.TravelPackageDto;
import cts.tpm.service.TravelPackageServiceImpl;

class TravelPackageControllerTest {

	private MockMvc mockMvc;

	@Mock
	private TravelPackageServiceImpl travelPackageService;

	@InjectMocks
	private TravelPackageController travelPackageController;

	private TravelPackageDto travelPackageDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(travelPackageController).build();

		travelPackageDto = new TravelPackageDto();
		travelPackageDto.setPackageId(1L);
		travelPackageDto.setTitle("Amazing Europe Trip");
		travelPackageDto.setDescription("A wonderful experience across Europe.");
		travelPackageDto.setDuration(10);
		travelPackageDto.setPrice(2500.0);
		travelPackageDto.setIncludeService("Flights, Hotels, Sightseeing");
	}

	@Test
	void testCreatePackage() throws Exception {
		when(travelPackageService.createPackage(any(TravelPackageDto.class))).thenReturn(travelPackageDto);

		mockMvc.perform(post("/api/packages").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"title\": \"Amazing Europe Trip\", \"description\": \"A wonderful experience "
						+ "across Europe.\", \"duration\": 10, \"price\": 2500.0, \"includeService\": \"Flights, "
						+ "Hotels, Sightseeing\" }"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Amazing Europe Trip"));
	}

	@Test
	void testGetAllPackages() throws Exception {
		List<TravelPackageDto> packages = Arrays.asList(travelPackageDto);
		when(travelPackageService.getAllPackages()).thenReturn(packages);

		mockMvc.perform(get("/api/packages")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(packages.size()));
	}

	@Test
	void testGetPackageById() throws Exception {
		when(travelPackageService.getPackageById(1L)).thenReturn(travelPackageDto);

		mockMvc.perform(get("/api/packages/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Amazing Europe Trip"));
	}

	@Test
	void testUpdatePackage() throws Exception {
		TravelPackageDto updatedDto = new TravelPackageDto();
		updatedDto.setPackageId(1L);
		updatedDto.setTitle("Updated Holiday");
		updatedDto.setDescription("New Itinerary");
		updatedDto.setDuration(7);
		updatedDto.setPrice(6000.0);
		updatedDto.setIncludeService("Hotel, Meals, Transport");

		when(travelPackageService.updatePackage(eq(1L), any(TravelPackageDto.class))).thenReturn(updatedDto);

		mockMvc.perform(put("/api/packages/1").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"title\": \"Updated Holiday\", \"description\": \"New Itinerary\", \"duration\":"
						+ " 7, \"price\": 6000.0, \"includeService\": \"Hotel, Meals, Transport\" }"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Updated Holiday"));
	}

	@Test
	void testDeletePackage() throws Exception {
		doNothing().when(travelPackageService).deletePackage(1L);

		mockMvc.perform(delete("/api/packages/1")).andExpect(status().isOk())
				.andExpect(content().string("Travel package deleted successfully."));
	}
}
