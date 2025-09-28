package com.theezyArt.theezyArtPortfolio.dto.request;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateArtworkRequest {
    @Id
    private String id;
    private String title;
    private String medium;
    private int year;
    private String size;
}
