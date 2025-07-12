package cts.tpm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private static final String UPLOAD_DIR = "uploads/packages/";
    private static final String GATEWAY_URL = "http://localhost:9999"; // API Gateway URL
    
    public String uploadImage(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Return the URL that can be accessed through API Gateway
        return GATEWAY_URL + "/api/packages/images/" + uniqueFilename;
    }
    
    public void deleteImage(String imageUrl) {
        try {
            // Extract the filename from the URL
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path path = Paths.get(UPLOAD_DIR, filename);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            // Log error but don't throw exception
            System.err.println("Error deleting image: " + imageUrl);
        }
    }
} 