package com.theezyArt.theezyArtPortfolio.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theezyArt.theezyArtPortfolio.data.model.IPAddress;
import com.theezyArt.theezyArtPortfolio.data.repositories.IPAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

@Service
public class IPAddressServiceImpl implements IPAddressService{

    @Autowired
    private IPAddressRepository ipAddressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public IPAddress geoIPLookup(String ipAddress) throws IOException, InterruptedException {

        if (ipAddress.equals("127.0.0.1") || ipAddress.equals("::1")) {
            System.out.println("This is your local IP");
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ip-api.com/json/" + ipAddress))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        IPAddress ipAddressInfo = objectMapper.readValue(response.body(), IPAddress.class);

        ipAddressRepository.save(ipAddressInfo);
//        System.out.println(response.body());
        return ipAddressInfo;
    }
}
