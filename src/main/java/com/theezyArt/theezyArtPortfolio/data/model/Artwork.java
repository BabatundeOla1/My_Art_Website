package com.theezyArt.theezyArtPortfolio.data.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Setter
@Getter
@Data
public class Artwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String title;
    private String medium;
    private int year;
    private String size;
    private String imageUrl;

}
