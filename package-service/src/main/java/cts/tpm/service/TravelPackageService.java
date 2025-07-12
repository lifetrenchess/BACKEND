package cts.tpm.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cts.tpm.model.TravelPackageDto;

public interface TravelPackageService {

	public TravelPackageDto createPackage(TravelPackageDto travelPackageDto);

	public List<TravelPackageDto> getAllPackages();

	public List<TravelPackageDto> getAllActivePackages();
	
	public List<TravelPackageDto> getPackagesByAgent(Long agentId);

	public List<TravelPackageDto> searchPackages(String destination, Double minPrice, Double maxPrice);

	public TravelPackageDto getPackageById(Long id);

	public TravelPackageDto updatePackage(Long id, TravelPackageDto updatedDto);

	public TravelPackageDto updatePackageStatus(Long id, boolean active);
	
	public TravelPackageDto updatePackageImage(Long id, String imagePath, boolean isMain);
	
	public List<String> addPackageImages(Long id, MultipartFile[] images) throws IOException;
	
	public void deletePackageImage(Long id, int imageIndex);

	public void deletePackage(Long id);
}
