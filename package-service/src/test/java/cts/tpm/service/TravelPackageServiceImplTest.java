package cts.tpm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import cts.tpm.entity.TravelPackage;
import cts.tpm.model.TravelPackageDto;
import cts.tpm.repository.TravelPackageRepository;

@ExtendWith(MockitoExtension.class)
class TravelPackageServiceImplTest {

	@Mock
	private TravelPackageRepository travelPackageRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private TravelPackageServiceImpl travelPackageService;

	private TravelPackage travelPackage;
	private TravelPackageDto travelPackageDto;

	@BeforeEach
	public void setUp() {
		travelPackage = TravelPackage.builder().packageId(1L).title("Amazing Europe Trip")
				.description("A wonderful experience across Europe.").duration(10).price(2500.0)
				.includeService("Flights, Hotels, Sightseeing").build();

		travelPackageDto = TravelPackageDto.builder().packageId(1L).title("Amazing Europe Trip")
				.description("A wonderful experience across Europe.").duration(10).price(2500.0)
				.includeService("Flights, Hotels, Sightseeing").build();
	}

	@Test
	void testCreatePackage() {
		when(modelMapper.map(travelPackageDto, TravelPackage.class)).thenReturn(travelPackage);
		when(travelPackageRepository.save(travelPackage)).thenReturn(travelPackage);
		when(modelMapper.map(travelPackage, TravelPackageDto.class)).thenReturn(travelPackageDto);

		TravelPackageDto createdDto = travelPackageService.createPackage(travelPackageDto);

		assertNotNull(createdDto);
		assertEquals(travelPackageDto.getTitle(), createdDto.getTitle());
		verify(travelPackageRepository, times(1)).save(travelPackage);
	}

	@Test
	void testGetAllPackages() {
		List<TravelPackage> packageList = Arrays.asList(travelPackage);
		when(travelPackageRepository.findAll()).thenReturn(packageList);
		when(modelMapper.map(travelPackage, TravelPackageDto.class)).thenReturn(travelPackageDto);

		List<TravelPackageDto> resultList = travelPackageService.getAllPackages();

		assertNotNull(resultList);
		assertFalse(resultList.isEmpty());
		assertEquals(1, resultList.size());
	}

	@Test
	void testGetPackageById() {
		when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
		when(modelMapper.map(travelPackage, TravelPackageDto.class)).thenReturn(travelPackageDto);

		TravelPackageDto resultDto = travelPackageService.getPackageById(1L);

		assertNotNull(resultDto);
		assertEquals(1L, resultDto.getPackageId());
	}

	// ✅ Test Update Package
	@Test
	void testUpdatePackage() {
		when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
		when(modelMapper.map(travelPackageDto, TravelPackage.class)).thenReturn(travelPackage);
		when(travelPackageRepository.save(travelPackage)).thenReturn(travelPackage);
		when(modelMapper.map(travelPackage, TravelPackageDto.class)).thenReturn(travelPackageDto);

		TravelPackageDto updatedDto = travelPackageService.updatePackage(1L, travelPackageDto);

		assertNotNull(updatedDto);
		assertEquals(travelPackageDto.getTitle(), updatedDto.getTitle());
		verify(travelPackageRepository, times(1)).save(travelPackage);
	}

	@Test
	void testDeletePackage() {
		when(travelPackageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));

		travelPackageService.deletePackage(1L);

		verify(travelPackageRepository, times(1)).delete(travelPackage);
	}
}
