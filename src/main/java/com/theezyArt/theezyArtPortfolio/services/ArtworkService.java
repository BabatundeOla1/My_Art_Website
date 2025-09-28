package com.theezyArt.theezyArtPortfolio.services;

import com.theezyArt.theezyArtPortfolio.data.model.Artwork;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.UpdateArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.DeleteArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.SaveArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.UpdateArtworkResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ArtworkService {

    // SaveArtworkResponse saveArtwork(SaveArtworkRequest saveArtworkRequest);
    SaveArtworkResponse saveArtwork(SaveArtworkRequest saveArtworkRequest);
    DeleteArtworkResponse deleteArtwork(String title);
    List<Artwork> getAllArtworks();

    UpdateArtworkResponse editArtwork(String title, UpdateArtworkRequest updateArtworkRequest);

}
