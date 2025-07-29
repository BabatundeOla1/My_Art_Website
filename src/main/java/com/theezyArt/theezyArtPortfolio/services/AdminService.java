package com.theezyArt.theezyArtPortfolio.services;

import com.theezyArt.theezyArtPortfolio.dto.request.AdminLoginRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.AdminLoginResponse;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public interface AdminService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    AdminLoginResponse login(AdminLoginRequest adminLoginRequest);
    void register();
}
