package com.theezyArt.theezyArtPortfolio.dto.response;

import lombok.Data;

@Data
public class UpdateArtworkResponse {
    private String title;
    private String id;
    private String medium;
    private int year;
    private String size;
    private String imageUrl;
    private String imagePath;
}
