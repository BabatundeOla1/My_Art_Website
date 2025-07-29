package com.theezyArt.theezyArtPortfolio.dto.response;

import lombok.Data;

@Data
public class SaveArtworkResponse {
    private String id;
    private String title;
    private String medium;
    private int year;
    private String size;
    private String imageUrl;
    private String imagePath;
}
