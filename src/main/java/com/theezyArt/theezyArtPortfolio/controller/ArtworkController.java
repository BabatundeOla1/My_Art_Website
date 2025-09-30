package com.theezyArt.theezyArtPortfolio.controller;

import com.theezyArt.theezyArtPortfolio.data.model.Artwork;
import com.theezyArt.theezyArtPortfolio.dto.request.SaveArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.request.UpdateArtworkRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.DeleteArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.SaveArtworkResponse;
import com.theezyArt.theezyArtPortfolio.dto.response.UpdateArtworkResponse;
import com.theezyArt.theezyArtPortfolio.services.AdminService;
import com.theezyArt.theezyArtPortfolio.services.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<SaveArtworkResponse> saveArtwork(
            @RequestParam("title") String title,
            @RequestParam("medium") String medium,
            @RequestParam("year") int year,
            @RequestParam("size") String size,
            @RequestPart("imageFile") MultipartFile imageFile) {

        SaveArtworkRequest request = new SaveArtworkRequest();
        request.setTitle(title);
        request.setMedium(medium);
        request.setYear(year);
        request.setSize(size);
        request.setImageFile(imageFile);

        SaveArtworkResponse response = artworkService.saveArtwork(request);
        return ResponseEntity.ok(response);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("editArtwork/{artworkId}")
    public ResponseEntity<UpdateArtworkResponse> editArtwork(@PathVariable("artworkId") String artworkId, @RequestBody UpdateArtworkRequest updateArtworkRequest){
        UpdateArtworkResponse updateResponse = artworkService.editArtwork(artworkId, updateArtworkRequest);
        return new ResponseEntity<>(updateResponse, HttpStatus.OK);
    }

}
