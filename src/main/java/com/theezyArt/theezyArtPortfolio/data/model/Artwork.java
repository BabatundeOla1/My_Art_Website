package com.theezyArt.theezyArtPortfolio.data.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@Data
public class Artwork {

    @Id
    private String id;
    private String title;
    private String medium;
    private int year;
    private String size;
    private String imageUrl;

}
