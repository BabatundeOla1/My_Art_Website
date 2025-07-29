package com.theezyArt.theezyArtPortfolio.data.repositories;

import com.theezyArt.theezyArtPortfolio.data.model.Artwork;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ArtworkRepository extends MongoRepository<Artwork, String> {
    Optional<Artwork> findArtworkByTitle(String title);
    Optional<Artwork> findArtworkById(String id);
}

