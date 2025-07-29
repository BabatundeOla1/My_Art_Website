package com.theezyArt.theezyArtPortfolio.data.repositories;

import com.theezyArt.theezyArtPortfolio.data.model.LoginHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginHistoryRepository extends MongoRepository<LoginHistory, String> {

}
