package com.theezyArt.theezyArtPortfolio.data.repositories;

import com.theezyArt.theezyArtPortfolio.data.model.IPAddress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPAddressRepository extends MongoRepository<IPAddress, String> {
}
