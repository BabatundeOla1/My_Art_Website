package com.theezyArt.theezyArtPortfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theezyArt.theezyArtPortfolio.data.repositories.AdminRepository;
import com.theezyArt.theezyArtPortfolio.data.repositories.ArtworkRepository;
import com.theezyArt.theezyArtPortfolio.dto.request.AdminLoginRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.AdminLoginResponse;
import com.theezyArt.theezyArtPortfolio.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    }

    @Test
    void testThatAuthenticatedAdmin_CanSaveArtwork() throws Exception{
        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImagePath("C:\\Users\\DELL USER\\Pictures\\Saved Pictures\\WhatsApp Image 2025-01-11 at 14.54.01_23807e85.jpg");
        request.setYear(2023);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/saveArtwork")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testThatAuthenticatedAdmin_CanDeleteSavedArtwork() throws Exception {
        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImagePath("C:\\Users\\DELL USER\\Pictures\\Saved Pictures\\WhatsApp Image 2025-01-11 at 14.54.01_23807e85.jpg");
        request.setYear(2023);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/saveArtwork")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/deleteArtwork")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("title", "Why Are We Here"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveArtworkWithAdminRole() throws Exception {
        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImagePath("C:\\Users\\DELL USER\\Pictures\\Saved Pictures\\WhatsApp Image 2025-01-11 at 14.54.01_23807e85.jpg");
        request.setYear(2023);

        mockMvc.perform(post("/api/admin/saveArtwork")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testThatArtworkCanBeDeleted() throws Exception {
        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle("Why Are We Here");
        request.setMedium("Acrylic on Canvas");
        request.setSize("20cm by 20cm");
        request.setImagePath("C:\\Users\\DELL USER\\Pictures\\Saved Pictures\\WhatsApp Image 2025-01-11 at 14.54.01_23807e85.jpg");
        request.setYear(2023);

        mockMvc.perform(post("/api/admin/saveArtwork")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/admin/deleteArtwork")
                        .param("title", request.getTitle()))
                .andExpect(status().isOk());
    }
}