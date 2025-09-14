package com.theezyArt.theezyArtPortfolio;

import com.theezyArt.theezyArtPortfolio.services.AdminService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TheezyArtPortfolioApplication {
	@Autowired
	private AdminService adminService;

	public static void main(String[] args) {
		SpringApplication.run(TheezyArtPortfolioApplication.class, args);
	}

	@PostConstruct
	public void init() {
		adminService.register();
	}

}
