package com.theezyArt.theezyArtPortfolio.dto.request;

import lombok.Data;

@Data
public class SaveArtworkRequest {
    private String title;
    private String medium;
    private int year;
    private String size;
    private String imagePath;
    private String imageUrl;
}
