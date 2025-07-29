package com.theezyArt.theezyArtPortfolio.controller;

import com.theezyArt.theezyArtPortfolio.data.repositories.AdminRepository;
import com.theezyArt.theezyArtPortfolio.dto.request.AdminLoginRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.AdminLoginResponse;
import com.theezyArt.theezyArtPortfolio.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/auth/")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest adminLoginRequest){
        AdminLoginResponse loginResponse = adminService.login(adminLoginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
