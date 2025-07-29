package com.theezyArt.theezyArtPortfolio.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CloudinaryServiceTest {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Test
    void testThatImageCanBeUploadedToCloudinary(){
        String filePath = "C:\\\\Users\\\\DELL USER\\\\Pictures\\\\my works\\\\A Guide To life_grid2.png";

        String uploadedUrl = cloudinaryService.uploadImage(filePath);

        assertNotNull(uploadedUrl);
        assertTrue(uploadedUrl.startsWith("https://res.cloudinary.com/"));
        System.out.println("Image uploaded to: " + uploadedUrl);
    }

    @Test
    void testThatImageCanBeUploadedAndDeletedFromCloudinary(){
        String filePath = "C:\\\\Users\\\\DELL USER\\\\Pictures\\\\my works\\\\A Guide To life_grid2.png";

        String uploadedUrl = cloudinaryService.uploadImage(filePath);

        assertNotNull(uploadedUrl);
        assertTrue(uploadedUrl.startsWith("https://res.cloudinary.com/"));

        boolean isDeleted = cloudinaryService.deleteImage(uploadedUrl);
        assertTrue(isDeleted);
    }
}