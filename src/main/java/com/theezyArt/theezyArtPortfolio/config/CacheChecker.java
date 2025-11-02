package com.theezyArt.theezyArtPortfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheChecker implements CommandLineRunner {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void run(String... args) {
        System.out.println("Active Cache Manager: " + cacheManager.getClass().getName());
    }
}
