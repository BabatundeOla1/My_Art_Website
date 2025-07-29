package com.theezyArt.theezyArtPortfolio.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class IPAddress {
    @Id
    private String id;
    private String country;
    private String regionName;
    private String city;
    private String zip;
    private Double lat;
    private Double lon;
    private String isp;
    private String timezone;
    private String query;

}
