package com.theezyArt.theezyArtPortfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class RedisConnectionChecker {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @PostConstruct
    public void checkConnection() {
        try (var conn = redisConnectionFactory.getConnection()) {
            System.out.println("Connected to Redis: " + conn.ping());
        } catch (Exception e) {
            System.out.println("Redis not reachable: " + e.getMessage());
        }
    }
}
