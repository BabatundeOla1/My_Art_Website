package com.theezyArt.theezyArtPortfolio.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName, @Value("${cloudinary.api-key}") String apiKey, @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String uploadImage(MultipartFile multipartFile) {
        try {
            Map<String, Object> options = ObjectUtils.asMap("folder", "ArtWebsite");
            Map<?, ?> response = cloudinary.uploader().upload(multipartFile.getBytes(), options);
            return response.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload to Cloudinary failed", e);
        }
    }

    public boolean deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Deleted test images: " + result);
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }

    public void deleteAllTestImages() {
        try {
            Map<String, Object> options = ObjectUtils.asMap("tag", "test-image");
            Map result = cloudinary.api().deleteResourcesByTag("test-image", options);
            System.out.println("Deleted test images: " + result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete test images from Cloudinary", e);
        }
    }


    private static void verifyFilePath(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
    }
    private String extractPublicId(String url) {
        try {
            //https://res.cloudinary.com/dqhye0rza/image/upload/v1752608593/ArtWebsite/d4tumz8z7iu7f3se8xcy.png
            String[] parts = url.split("/upload/");
            if (parts.length != 2){
                throw new IllegalArgumentException("Invalid Cloudinary URL format: " + url);
            }

            //v1752608593/Art%20Website/d4tumz8z7iu7f3se8xcy.png (Split Off Version'v17..')
            String pathAfterUpload = parts[1];
            String[] pathParts = pathAfterUpload.split("/", 2);
            if (pathParts.length != 2) {
                throw new IllegalArgumentException("Could not extract path after version in URL: " + url);
            }

            //Art%20Website/d4tumz8z7iu7f3se8xcy.png
            String publicIdWithExtension = pathParts[1];
            int dotIndex = publicIdWithExtension.lastIndexOf('.');
            return (dotIndex == -1) ? publicIdWithExtension : publicIdWithExtension.substring(0, dotIndex);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract public ID from Cloudinary URL", e);
        }
    }


}