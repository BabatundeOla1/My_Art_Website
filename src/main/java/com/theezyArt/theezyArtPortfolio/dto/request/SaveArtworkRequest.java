package com.theezyArt.theezyArtPortfolio.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaveArtworkRequest {
    private String title;
    private String medium;
    private int year;
    private String size;
    private String imagePath;
//    private MultipartFile imagePath;
    private String imageUrl;

}
