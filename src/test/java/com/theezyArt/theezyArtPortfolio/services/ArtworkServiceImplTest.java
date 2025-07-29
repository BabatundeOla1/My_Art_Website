package com.theezyArt.theezyArtPortfolio.services;

import static org.junit.jupiter.api.Assertions.*;

import com.theezyArt.theezyArtPortfolio.data.model.Artwork;
import com.theezyArt.theezyArtPortfolio.data.repositories.ArtworkRepository;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.UpdateArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.DeleteArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.SaveArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.UpdateArtworkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@SpringBootTest
class ArtworkServiceImplTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);


    @Autowired
    private ArtworkService artworkService;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void clearArtworkDatabase(){
        artworkRepository.deleteAll();
//        cloudinaryService.deleteAllTestImages();
    }

    void setUpArtwork(SaveArtworkRequest saveArtworkRequest){
        saveArtworkRequest.setYear(2025);
        saveArtworkRequest.setMedium("Acrylic on Canvas");
        saveArtworkRequest.setImagePath("C:\\Users\\DELL USER\\Pictures\\my works\\A Guide To life_grid2.png");
        saveArtworkRequest.setTitle("test-image Memories");
        saveArtworkRequest.setSize("70cm by 70cm");
    }
    void setUpdateArtworkRequest(UpdateArtworkRequest updateArtworkRequest){
        updateArtworkRequest.setYear(2025);
        updateArtworkRequest.setMedium("Acrylic on Canvas");
        updateArtworkRequest.setImagePath("C:\\Users\\DELL USER\\Pictures\\my works\\A Guide To life_grid2.png");
        updateArtworkRequest.setTitle("test-image Memories Updated");
        updateArtworkRequest.setSize("70cm by 70cm");
    }
    void setUpSecondArtwork(SaveArtworkRequest secondArtwork){
        secondArtwork.setYear(2025);
        secondArtwork.setMedium("Acrylic on Canvas");
        secondArtwork.setImagePath("C:\\Users\\DELL USER\\Pictures\\my works\\Pinterest\\download (1).jpg");
        secondArtwork.setTitle("test-image Moment of reflection");
        secondArtwork.setSize("40cm by 70cm");
    }

    @Test
    void testThatArtworkCanBeSaved() {
        SaveArtworkRequest saveArtworkRequest = new SaveArtworkRequest();
        setUpArtwork(saveArtworkRequest);

        SaveArtworkResponse savedResponse = artworkService.saveArtwork(saveArtworkRequest);

        assertEquals("test-image Memories", savedResponse.getTitle());
        assertEquals("70cm by 70cm", savedResponse.getSize());
        assertThat(savedResponse).isNotNull();
        assertThat(savedResponse.getImageUrl()).isNotEmpty();
        assertEquals(1, artworkRepository.count());
        System.out.println("Artwork ImageURL: " + savedResponse.getImageUrl());
    }

    @Test
    void TestThatArtworkCanBeDeleted(){
        SaveArtworkRequest saveArtworkRequest = new SaveArtworkRequest();
        setUpArtwork(saveArtworkRequest);

        SaveArtworkResponse savedResponse = artworkService.saveArtwork(saveArtworkRequest);

        assertThat(savedResponse).isNotNull();
        assertEquals(1, artworkRepository.count());

        DeleteArtworkResponse deletedArtwork = artworkService.deleteArtwork(saveArtworkRequest.getTitle());
        assertEquals(0, artworkRepository.count());
        assertThat(deletedArtwork).isNotNull();
    }

    @Test
    void testThatListOfArtworksCanBeViewed(){
        SaveArtworkRequest firstArtworkRequest = new SaveArtworkRequest();
        setUpArtwork(firstArtworkRequest);
        SaveArtworkResponse firstResponse = artworkService.saveArtwork(firstArtworkRequest);

        SaveArtworkRequest secondArtworkRequest = new SaveArtworkRequest();
        setUpSecondArtwork(secondArtworkRequest);
        SaveArtworkResponse secondResponse = artworkService.saveArtwork(secondArtworkRequest);

        assertNotNull(firstResponse);
        assertNotNull(secondResponse);
        assertThat(firstArtworkRequest).isNotNull();
        assertThat(secondResponse).isNotNull();
        assertEquals(2, artworkRepository.count());

        List<Artwork> allArtworks = artworkService.getAllArtworks();
        assertEquals(2, allArtworks.size());
        assertTrue(allArtworks.stream().anyMatch(art -> art.getTitle().equals(firstArtworkRequest.getTitle())));
        assertTrue(allArtworks.stream().anyMatch(art -> art.getTitle().equals(secondArtworkRequest.getTitle())));

        try {
            String json = objectMapper.writeValueAsString(allArtworks);
            System.out.println("All artworks in JSON:\n" + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testThatASavedArtworkCanBeUpdated(){
        SaveArtworkRequest saveArtworkRequest = new SaveArtworkRequest();
        setUpArtwork(saveArtworkRequest);

        SaveArtworkResponse savedResponse = artworkService.saveArtwork(saveArtworkRequest);

        assertThat(savedResponse).isNotNull();
        assertEquals(1, artworkRepository.count());

        UpdateArtworkRequest artworkRequest = new UpdateArtworkRequest();
        setUpdateArtworkRequest(artworkRequest);

        UpdateArtworkResponse updatedArtwork = artworkService.editArtwork(savedResponse.getTitle(), artworkRequest);
        assertNotEquals("test-image Memories", updatedArtwork.getTitle());
        assertEquals("test-image Memories Updated", updatedArtwork.getTitle());
        System.out.println("Saved artwork response: " + savedResponse);

    }
}