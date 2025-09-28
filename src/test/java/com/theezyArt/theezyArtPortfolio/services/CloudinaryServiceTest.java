package com.theezyArt.theezyArtPortfolio.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
class CloudinaryServiceTest {

    @Autowired
    private CloudinaryService cloudinaryService;

//    @Test
//    void testThatImageCanBeUploadedToCloudinary(){
//        String filePath = "C:\\\\Users\\\\DELL USER\\\\Pictures\\\\my works\\\\A Guide To life_grid2.png";
//
//        String uploadedUrl = cloudinaryService.uploadImage(filePath);
//
//        assertNotNull(uploadedUrl);
//        assertTrue(uploadedUrl.startsWith("https://res.cloudinary.com/"));
//        System.out.println("Image uploaded to: " + uploadedUrl);
//    }

//    @Test
//    void testThatImageCanBeUploadedAndDeletedFromCloudinary(){
//        String filePath = "C:\\\\Users\\\\DELL USER\\\\Pictures\\\\my works\\\\A Guide To life_grid2.png";
//
//        String uploadedUrl = cloudinaryService.uploadImage(filePath);
//
//        assertNotNull(uploadedUrl);
//        assertTrue(uploadedUrl.startsWith("https://res.cloudinary.com/"));
//
//        boolean isDeleted = cloudinaryService.deleteImage(uploadedUrl);
//        assertTrue(isDeleted);
//    }

//    @Test
//    void testThatImageCanBeUploadedToCloudinary() throws IOException {
//        // Load file from local disk
//        FileInputStream filePath = new FileInputStream("C:\\Users\\DELL USER\\Pictures\\my works\\A Guide To life_grid2.png");
//
//        MultipartFile multipartFile = new MockMultipartFile(
//                "file",                                  // form field name
//                "A Guide To life_grid2.png",             // original filename
//                "image/png",                             // content type
//                filePath                                      // file content
//        );
//        String uploadedUrl = cloudinaryService.uploadImage(multipartFile);
//
//        assertNotNull(uploadedUrl);
//        assertTrue(uploadedUrl.startsWith("https://res.cloudinary.com/"));
//        System.out.println("Image uploaded to: " + uploadedUrl);
//    }
//
//    @Test
//    void testThatImageCanBeUploadedAndDeletedFromCloudinary() throws IOException {
//        FileInputStream fis = new FileInputStream("C:\\Users\\DELL USER\\Pictures\\my works\\A Guide To life_grid2.png");
//
//        MultipartFile multipartFile = new MockMultipartFile(
//                "file",
//                "A Guide To life_grid2.png",
//                "image/png",
//                fis
//        );
//        String uploadedUrl = cloudinaryService.uploadImage(multipartFile);
//
//        assertNotNull(uploadedUrl);
//        assertTrue(uploadedUrl.startsWith("https://res.cloudinary.com/"));
//
//        boolean isDeleted = cloudinaryService.deleteImage(uploadedUrl);
//        assertTrue(isDeleted);
//    }

}