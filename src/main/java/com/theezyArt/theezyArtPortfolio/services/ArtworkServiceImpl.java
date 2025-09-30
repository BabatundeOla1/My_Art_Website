package com.theezyArt.theezyArtPortfolio.services;


import com.theezyArt.theezyArtPortfolio.data.model.Artwork;
import com.theezyArt.theezyArtPortfolio.data.repositories.ArtworkRepository;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.UpdateArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.DeleteArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.SaveArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.UpdateArtworkResponse;
import com.theezyArt.theezyArtPortfolio.utils.exceptions.ArtworkNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService{
    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public SaveArtworkResponse saveArtwork(SaveArtworkRequest saveArtworkRequest) {
        String imageUrl = cloudinaryService.uploadImage(saveArtworkRequest.getImageFile());
        saveArtworkRequest.setImageUrl(imageUrl);

        Artwork newArtwork = modelMapper.map(saveArtworkRequest, Artwork.class);
        Artwork savedArtwork = artworkRepository.save(newArtwork);
        return modelMapper.map(savedArtwork, SaveArtworkResponse.class);
    }

    @Override
    public DeleteArtworkResponse deleteArtwork(String title) {

        Artwork foundArtwork = artworkRepository.findArtworkByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Artwork not found"));

        cloudinaryService.deleteImage(foundArtwork.getImageUrl());
        artworkRepository.delete(foundArtwork);
        DeleteArtworkResponse response = new DeleteArtworkResponse();
        response.setMessage("'\" + title + \"' deleted successfully.");
        return response;
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    @Override
    public UpdateArtworkResponse editArtwork(String artworkId, UpdateArtworkRequest updateArtworkRequest) {

        Artwork foundArtwork = artworkRepository.findArtworkById(artworkId)
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork not found"));

        verifyArtworkDetails(updateArtworkRequest, foundArtwork);

        Artwork updatedArtwork = artworkRepository.save(foundArtwork);
        return modelMapper.map(updatedArtwork, UpdateArtworkResponse.class);
    }

    private static void verifyArtworkDetails(UpdateArtworkRequest updateArtworkRequest, Artwork foundArtwork) {
        if (updateArtworkRequest.getTitle() != null && !updateArtworkRequest.getTitle().isBlank()) {
            foundArtwork.setTitle(updateArtworkRequest.getTitle());
        }
        if (updateArtworkRequest.getMedium() != null && !updateArtworkRequest.getMedium().isBlank()) {
            foundArtwork.setMedium(updateArtworkRequest.getMedium());
        }
        if (updateArtworkRequest.getSize() != null && !updateArtworkRequest.getSize().isBlank()) {
            foundArtwork.setSize(updateArtworkRequest.getSize());
        }
        if (updateArtworkRequest.getYear() > 0) {
            foundArtwork.setYear(updateArtworkRequest.getYear());
        }
    }
}
