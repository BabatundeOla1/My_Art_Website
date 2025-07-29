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

        String imageUrl = cloudinaryService.uploadImage(saveArtworkRequest.getImagePath());
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
    public UpdateArtworkResponse editArtwork(String title, UpdateArtworkRequest updateArtworkRequest) {

        Artwork foundArtwork = artworkRepository.findArtworkByTitle(title)
                .orElseThrow(() -> new ArtworkNotFoundException("Artwork not found"));

        foundArtwork.setTitle(updateArtworkRequest.getTitle());
        foundArtwork.setSize(updateArtworkRequest.getSize());
        foundArtwork.setYear(updateArtworkRequest.getYear());
        foundArtwork.setMedium(updateArtworkRequest.getMedium());

        Artwork updatedArtwork = artworkRepository.save(foundArtwork);
        return modelMapper.map(updatedArtwork, UpdateArtworkResponse.class);
    }
}
