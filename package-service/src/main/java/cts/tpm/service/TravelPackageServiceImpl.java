package cts.tpm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cts.tpm.client.UserClient;
import cts.tpm.entity.TravelPackage;
import cts.tpm.exception.ResourceNotFoundException;
import cts.tpm.model.TravelPackageDto;
import cts.tpm.model.UserDto;
import cts.tpm.repository.TravelPackageRepository;
import cts.tpm.service.FileUploadService;
import jakarta.validation.Valid;

@Service
public class TravelPackageServiceImpl implements TravelPackageService {

	@Autowired
	private TravelPackageRepository travelPackageRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserClient userClient;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public TravelPackageDto createPackage(@Valid TravelPackageDto travelPackageDto) {
		TravelPackage travelPackage = convertDtoToEntity(travelPackageDto);
		if (travelPackage.getFlights() != null) {
			travelPackage.getFlights().forEach(flight -> flight.setTravelPackage(travelPackage));
		}
		if (travelPackage.getHotels() != null) {
			travelPackage.getHotels().forEach(hotel -> hotel.setTravelPackage(travelPackage));
		}
		if (travelPackage.getSightseeingList() != null) {
			travelPackage.getSightseeingList().forEach(sightseeing -> sightseeing.setTravelPackage(travelPackage));
		}
		TravelPackage saved = travelPackageRepository.save(travelPackage);
		return convertEntityToDto(saved);
	}

	@Override
	public List<TravelPackageDto> getAllPackages() {
		List<TravelPackage> packages = travelPackageRepository.findAll();
		return packages.stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<TravelPackageDto> getAllActivePackages() {
		List<TravelPackage> packages = travelPackageRepository.findByActiveTrue();
		return packages.stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}
	
	@Override
	public List<TravelPackageDto> getPackagesByAgent(Long agentId) {
		List<TravelPackage> packages = travelPackageRepository.findByCreatedByAgentId(agentId);
		return packages.stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<TravelPackageDto> searchPackages(String destination, Double minPrice, Double maxPrice) {
		List<TravelPackage> packages = travelPackageRepository.findByActiveTrue();
		
		return packages.stream()
				.filter(pkg -> {
					// Filter by destination (title, description, or destination field)
					if (destination != null && !destination.trim().isEmpty()) {
						String destLower = destination.toLowerCase();
						boolean matchesDestination = (pkg.getTitle() != null && pkg.getTitle().toLowerCase().contains(destLower)) ||
								(pkg.getDescription() != null && pkg.getDescription().toLowerCase().contains(destLower)) ||
								(pkg.getDestination() != null && pkg.getDestination().toLowerCase().contains(destLower));
						if (!matchesDestination) return false;
					}
					
					// Filter by price range
					if (minPrice != null && pkg.getPrice() < minPrice) return false;
					if (maxPrice != null && pkg.getPrice() > maxPrice) return false;
					
					return true;
				})
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public TravelPackageDto getPackageById(Long id) {
		TravelPackage travelPackage = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
		return convertEntityToDto(travelPackage);
	}

	@Override
	public TravelPackageDto updatePackage(Long id, TravelPackageDto updatedDto) {
		TravelPackage existing = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

		TravelPackage updatedEntity = convertDtoToEntity(updatedDto);
		updatedEntity.setPackageId(id); // preserving the ID

		if (updatedEntity.getFlights() != null) {
			updatedEntity.getFlights().forEach(flight -> flight.setTravelPackage(updatedEntity));
		}
		if (updatedEntity.getHotels() != null) {
			updatedEntity.getHotels().forEach(hotel -> hotel.setTravelPackage(updatedEntity));
		}
		if (updatedEntity.getSightseeingList() != null) {
			updatedEntity.getSightseeingList().forEach(sightseeing -> sightseeing.setTravelPackage(updatedEntity));
		}
		TravelPackage saved = travelPackageRepository.save(updatedEntity);
		return convertEntityToDto(saved);
	}

	@Override
	public TravelPackageDto updatePackageStatus(Long id, boolean active) {
		TravelPackage existing = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
		
		existing.setActive(active);
		TravelPackage saved = travelPackageRepository.save(existing);
		return convertEntityToDto(saved);
	}
	
	@Override
	public TravelPackageDto updatePackageImage(Long id, String imagePath, boolean isMain) {
		TravelPackage existing = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
		
		if (isMain) {
			existing.setMainImage(imagePath);
		} else {
			// Add to additional images
			List<String> images = getImagesList(existing.getImages());
			images.add(imagePath);
			existing.setImages(convertImagesListToString(images));
		}
		
		TravelPackage saved = travelPackageRepository.save(existing);
		return convertEntityToDto(saved);
	}
	
	@Override
	public List<String> addPackageImages(Long id, MultipartFile[] images) throws IOException {
		TravelPackage existing = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
		
		List<String> imagePaths = new ArrayList<>();
		for (MultipartFile image : images) {
			String imagePath = fileUploadService.uploadImage(image);
			imagePaths.add(imagePath);
		}
		
		// Add to existing images
		List<String> existingImages = getImagesList(existing.getImages());
		existingImages.addAll(imagePaths);
		existing.setImages(convertImagesListToString(existingImages));
		
		travelPackageRepository.save(existing);
		return imagePaths;
	}
	
	@Override
	public void deletePackageImage(Long id, int imageIndex) {
		TravelPackage existing = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
		
		List<String> images = getImagesList(existing.getImages());
		if (imageIndex >= 0 && imageIndex < images.size()) {
			String imageToDelete = images.get(imageIndex);
			fileUploadService.deleteImage(imageToDelete);
			images.remove(imageIndex);
			existing.setImages(convertImagesListToString(images));
			travelPackageRepository.save(existing);
		}
	}

	@Override
	public void deletePackage(Long id) {
		TravelPackage travelPackage = travelPackageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
		
		// Delete associated images
		if (travelPackage.getMainImage() != null) {
			fileUploadService.deleteImage(travelPackage.getMainImage());
		}
		if (travelPackage.getImages() != null) {
			List<String> images = getImagesList(travelPackage.getImages());
			images.forEach(fileUploadService::deleteImage);
		}
		
		travelPackageRepository.delete(travelPackage);
	}

	// Custom conversion methods to handle String <-> List<String> mapping
	private TravelPackageDto convertEntityToDto(TravelPackage entity) {
		TravelPackageDto dto = modelMapper.map(entity, TravelPackageDto.class);
		// Convert String images to List<String>
		if (entity.getImages() != null) {
			dto.setImages(getImagesList(entity.getImages()));
		} else {
			dto.setImages(new ArrayList<>());
		}
		return dto;
	}
	
	private TravelPackage convertDtoToEntity(TravelPackageDto dto) {
		TravelPackage entity = modelMapper.map(dto, TravelPackage.class);
		// Convert List<String> images to String
		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			entity.setImages(convertImagesListToString(dto.getImages()));
		} else {
			entity.setImages("[]");
		}
		return entity;
	}

	// Helper methods for image management
	private List<String> getImagesList(String imagesJson) {
		if (imagesJson == null || imagesJson.trim().isEmpty()) {
			return new ArrayList<>();
		}
		try {
			return Arrays.asList(objectMapper.readValue(imagesJson, String[].class));
		} catch (JsonProcessingException e) {
			return new ArrayList<>();
		}
	}
	
	private String convertImagesListToString(List<String> images) {
		try {
			return objectMapper.writeValueAsString(images);
		} catch (JsonProcessingException e) {
			return "[]";
		}
	}

	// Example method to fetch user info using FeignClient
	public UserDto getUserInfoFromUserService(Long userId) {
		return userClient.getUserById(userId);
	}
}
