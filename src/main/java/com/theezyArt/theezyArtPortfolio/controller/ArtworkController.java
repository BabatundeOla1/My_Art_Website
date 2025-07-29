package com.theezyArt.theezyArtPortfolio.controller;

import com.theezyArt.theezyArtPortfolio.data.model.Artwork;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.DeleteArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.SaveArtworkResponse;
import com.theezyArt.theezyArtPortfolio.services.AdminService;
import com.theezyArt.theezyArtPortfolio.services.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin/")
@RestController
public class ArtworkController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ArtworkService artworkService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("saveArtwork")
    public ResponseEntity<SaveArtworkResponse> saveArtwork(@RequestBody SaveArtworkRequest saveArtworkRequest){
        SaveArtworkResponse response = artworkService.saveArtwork(saveArtworkRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deleteArtwork")
    public ResponseEntity<DeleteArtworkResponse> deleteArtwork(@RequestParam String title){
        DeleteArtworkResponse deleteResponse = artworkService.deleteArtwork(title);
        return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
    }

    @GetMapping("viewAllArtworks")
    public List<Artwork> getAllArtworks(){
        return artworkService.getAllArtworks();
    }

}
