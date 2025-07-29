package com.theezyArt.theezyArtPortfolio.services;

import com.theezyArt.theezyArtPortfolio.data.model.IPAddress;
import org.springframework.stereotype.Service;

import java.io.IOException;


public interface IPAddressService {

    IPAddress geoIPLookup(String ipAddress) throws IOException, InterruptedException;
}
