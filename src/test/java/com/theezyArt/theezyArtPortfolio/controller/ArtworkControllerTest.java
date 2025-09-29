package com.theezyArt.theezyArtPortfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theezyArt.theezyArtPortfolio.data.repositories.AdminRepository;
import com.theezyArt.theezyArtPortfolio.data.repositories.ArtworkRepository;
import com.theezyArt.theezyArtPortfolio.dto.request.AdminLoginRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.UpdateArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.AdminLoginResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.SaveArtworkResponse;
import com.theezyArt.theezyArtPortfolio.services.AdminService;
import com.theezyArt.theezyArtPortfolio.services.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArtworkControllerTest {

    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminService adminService;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;
    @MockBean
    private CloudinaryService cloudinaryService;


    private String adminToken;

    @BeforeEach
    void setUpAdminLogin(){
        adminRepository.deleteAll();
        artworkRepository.deleteAll();

        adminService.register();

        AdminLoginRequest admin = new AdminLoginRequest();
        admin.setEmail(adminEmail);
        admin.setPassword(adminPassword);

        AdminLoginResponse adminLoginResponse = adminService.login(admin);
        adminToken = adminLoginResponse.getAccessToken();

        when(cloudinaryService.uploadImage(any(MultipartFile.class)))
                .thenReturn("https://cloudinary.com/test-image.jpg");
    }

    void setUpdateArtworkRequest(UpdateArtworkRequest updateArtworkRequest){

        updateArtworkRequest.setYear(2025);
        updateArtworkRequest.setMedium("Acrylic on Canvas");
//        updateArtworkRequest.setImagePath("C:\\Users\\DELL USER\\Pictures\\my works\\A Guide To life_grid2.png");
        updateArtworkRequest.setTitle("Testing IntegrationTest To Change Artwork Title");
        updateArtworkRequest.setSize("70cm by 70cm");
    }

    @Test
    void testThatAuthenticatedAdmin_CanSaveArtwork() throws Exception{

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImageFile(imageFile);
        request.setYear(2023);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/saveArtwork")
                        .file(imageFile)
                        .param("title", "Why Are We Here")
                        .param("medium", "Acrylic on Canvas")
                        .param("size", "20cm by 20cm")
                        .param("year", "2023")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void testThatAuthenticatedAdmin_CanDeleteSavedArtwork() throws Exception {

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImageFile(imageFile);
        request.setYear(2023);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/saveArtwork")
                        .file(imageFile)
                        .param("title", "Why Are We Here")
                        .param("medium", "Acrylic on Canvas")
                        .param("size", "20cm by 20cm")
                        .param("year", "2023")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/deleteArtwork")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("title", "Why Are We Here"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveArtworkWithAdminRole() throws Exception {

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImageFile(imageFile);
        request.setYear(2023);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/saveArtwork")
                        .file(imageFile)
                        .param("title", "Why Are We Here")
                        .param("medium", "Acrylic on Canvas")
                        .param("size", "20cm by 20cm")
                        .param("year", "2023")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testThatArtworkCanBeDeleted() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "test.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImageFile(imageFile);
        request.setYear(2023);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/saveArtwork")
                        .file(imageFile)
                        .param("title", "Why Are We Here")
                        .param("medium", "Acrylic on Canvas")
                        .param("size", "20cm by 20cm")
                        .param("year", "2023")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/deleteArtwork")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("title", "Why Are We Here"))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testThatSavedArtworkCanBeUpdated() throws Exception{
//        MockMultipartFile imageFile = new MockMultipartFile(
//                "imageFile",
//                "test.jpg",
//                "image/jpeg",
//                "fake-image-content".getBytes()
//        );
//
//        SaveArtworkRequest request = new SaveArtworkRequest();
//        request.setTitle("Why Are We Here");
//        request.setMedium("Acrylic on Canvas");
//        request.setSize("20cm by 20cm");
//        request.setImageFile(imageFile);
//        request.setYear(2023);
//
//        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/saveArtwork")
//                        .file(imageFile)
//                        .param("title", "Why Are We Here")
//                        .param("medium", "Acrylic on Canvas")
//                        .param("size", "20cm by 20cm")
//                        .param("year", "2023")
//                        .header("Authorization", "Bearer " + adminToken)
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk());
//
//        SaveArtworkResponse saveArtworkResponse = objectMapper.readValue(response.getContentAsString(), SaveArtworkResponse.class);
//
//        UpdateArtworkRequest updateArtworkRequest = new UpdateArtworkRequest();
//        setUpdateArtworkRequest(updateArtworkRequest);
//
//        mockMvc.perform(post("/api/admin/editArtwork/" + saveArtworkResponse.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updateArtworkRequest)))
//                .andExpect(status().isOk());
//    }
}