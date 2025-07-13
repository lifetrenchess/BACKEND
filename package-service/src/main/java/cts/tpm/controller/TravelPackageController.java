package cts.tpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cts.tpm.model.TravelPackageDto;
import cts.tpm.service.TravelPackageServiceImpl;
import cts.tpm.service.FileUploadService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/packages")
public class TravelPackageController {

	@Autowired
	private TravelPackageServiceImpl travelPackageService;
	
	@Autowired
	private FileUploadService fileUploadService;

	@PostMapping
	public ResponseEntity<?> createPackage(
			@RequestParam(value = "mainImage", required = false) MultipartFile mainImage,
			@RequestParam(value = "additionalImages", required = false) MultipartFile[] additionalImages,
			@RequestParam("packageData") String packageData) {
		
		try {
			// Parse package data from JSON string
			ObjectMapper mapper = new ObjectMapper();
			TravelPackageDto travelPackageDto = mapper.readValue(packageData, TravelPackageDto.class);
			
			// Handle main image upload
			if (mainImage != null && !mainImage.isEmpty()) {
				String mainImageUrl = fileUploadService.uploadImage(mainImage);
				travelPackageDto.setMainImage(mainImageUrl);
			}
			
			// Handle additional images upload
			if (additionalImages != null && additionalImages.length > 0) {
				List<String> additionalImageUrls = new ArrayList<>();
				for (MultipartFile image : additionalImages) {
					if (image != null && !image.isEmpty()) {
						String imageUrl = fileUploadService.uploadImage(image);
						additionalImageUrls.add(imageUrl);
					}
				}
				travelPackageDto.setImages(additionalImageUrls);
			}
			
			// Create package with all data including images
			TravelPackageDto createdPackage = travelPackageService.createPackage(travelPackageDto);
			return ResponseEntity.ok(createdPackage);
			
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to create package: " + e.getMessage());
		}
	}
	
	@PostMapping("/{id}/image")
	public ResponseEntity<String> uploadPackageImage(
			@PathVariable Long id,
			@RequestParam("image") MultipartFile image,
			@RequestParam(value = "isMain", defaultValue = "false") boolean isMain) {
		try {
			String imagePath = fileUploadService.uploadImage(image);
			travelPackageService.updatePackageImage(id, imagePath, isMain);
			return ResponseEntity.ok(imagePath);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to update package image: " + e.getMessage());
		}
	}
	
	@PostMapping("/{id}/images")
	public ResponseEntity<?> uploadPackageImages(
			@PathVariable Long id,
			@RequestParam("images") MultipartFile[] images) {
		try {
			List<String> imagePaths = travelPackageService.addPackageImages(id, images);
			return ResponseEntity.ok(imagePaths);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Failed to upload images: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to add package images: " + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<List<TravelPackageDto>> getAllPackages() {
		return ResponseEntity.ok(travelPackageService.getAllActivePackages());
	}

	@GetMapping("/all")
	public ResponseEntity<List<TravelPackageDto>> getAllPackagesIncludingInactive() {
		return ResponseEntity.ok(travelPackageService.getAllPackages());
	}
	
	@GetMapping("/agent/{agentId}")
	public ResponseEntity<List<TravelPackageDto>> getPackagesByAgent(@PathVariable Long agentId) {
		return ResponseEntity.ok(travelPackageService.getPackagesByAgent(agentId));
	}

	@GetMapping("/search")
	public ResponseEntity<List<TravelPackageDto>> searchPackages(
			@RequestParam(required = false) String destination,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice) {
		return ResponseEntity.ok(travelPackageService.searchPackages(destination, minPrice, maxPrice));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPackageById(@PathVariable Long id) {
		try {
			TravelPackageDto packageDto = travelPackageService.getPackageById(id);
			return ResponseEntity.ok(packageDto);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePackage(@PathVariable Long id,
			@Valid @RequestBody TravelPackageDto travelPackageDto) {
		try {
			TravelPackageDto updatedPackage = travelPackageService.updatePackage(id, travelPackageDto);
			return ResponseEntity.ok(updatedPackage);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to update package: " + e.getMessage());
		}
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<?> updatePackageStatus(@PathVariable Long id,
			@RequestParam boolean active) {
		try {
			TravelPackageDto updatedPackage = travelPackageService.updatePackageStatus(id, active);
			return ResponseEntity.ok(updatedPackage);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to update package status: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePackage(@PathVariable Long id) {
		try {
			travelPackageService.deletePackage(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to delete package: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}/image/{imageIndex}")
	public ResponseEntity<?> deletePackageImage(@PathVariable Long id, @PathVariable int imageIndex) {
		try {
			travelPackageService.deletePackageImage(id, imageIndex);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Failed to delete package image: " + e.getMessage());
		}
	}
}